package com.shozab

@Grab("thymeleaf-spring4")
@Log
@Controller
class Crawler implements CommandLineRunner {

    @Value('${domain:spring.io}')
    String domain
    
    @Value('${exclude:jira.spring.io,forum.spring.io,repo.spring.io}')
    String[] excluded_domains

    @Value('${depth:-1}')
    int level_limit
    
    def links = []
    def dead_links = []
	def internal_links =[]
	def external_links =[]
	def image_links =[]
    def paths = [:]

    def strip(link) {
        if (link.endsWith("/")) {
            link[0..-2]
        } else {
            link
        }
    }

    def convert(link) {      
        if (link.startsWith("http")) {
            return strip(link)
        }
    
        if (link.startsWith("//")) {
            return strip("http:${link}")
        }
    
        if (link.startsWith("/")) {
            def l = strip("http://${domain}${link}")
            //log.info "Returning ${l}"
            return l
        }
    
        return strip("http://${link}")
    }

    def scan_for_links(url, links, internal_links, dead_links, external_links, image_links, paths, levels) {
        levels += 1
        if (level_limit > 0 && levels > level_limit) {
            log.info "You have exceeded the limit of ${level_limit}. Dropping back"
            return
        }

        log.info "Crawling ${url}"

        def html = url.toURL().text

        def local_links = []

        def m = ( html =~ /href="([a-zA-Z0-9\/\.\?-]+)"/ )
        //log.info "Found ${m.size()} links"

        for (link in m) {
            def converted_link = convert(link[1])
            if (!links.contains(converted_link)) {
                //log.info "Adding ${converted_link} to list"
                links << converted_link
                local_links << converted_link
            } else {
                //log.info "${converted_link} is already in it!"
            }	
        }

        for (link in local_links) {
            //log.info "Newly found! ${link}"
        }

        if (local_links.size() > 0) {
            for (link in local_links) {
                // TODO: Change to REGEX and include PDF, etc 
			    if ( (link.contains(".png")) || link.contains(".jpg") ){
						image_links << [url, link]
						continue
				} else if (link.contains(domain)) {
                    def hostname = new URL(link).host
                    //log.info "Checking ${excluded_domains} for ${hostname}"
                    if (excluded_domains.contains(hostname)) {
                        log.info "${hostname} is on the excluded list, so skipping"
                        continue
					} else {
                        //log.info "${hostname} is NOT excluded, so going deeper"
						internal_links << [url, link]
                    }
                    try {
                        scan_for_links(link, links, internal_links, dead_links, external_links, image_links, paths, levels)
                        if (paths.containsKey(link)) {
                            paths[link] << url
                        } else {
                            paths[link] = [url]
                        }
                    } catch(/*urllib2.HTTPError*/ e) {
                        log.severe "Can't find link from ${url} to ${link}"
                        log.severe e.toString()
                        dead_links << [url, link]
                        //if (e.code == 404) {
                        //    dead_links << [url, link]
                        //    levels -= 1
                        //    return
                        //}
                    }
                } else {
                    log.info "${link} leaves this domain, so not crawling"
					external_links << [url, link]
                }
            }
        } else {
            //log.info "No new links to crawl"
        }

        levels -= 1  
    }
    
    void run(String[] args) { 
        def levels = 0
      
        log.info("About to scan ${domain}...")
        log.info("...unless it goes into ${excluded_domains}")
        log.info("Will only go ${level_limit==-1?'infinite':level_limit} level${level_limit==1?'':'s'} deep")
        
        def base = "http://${domain}"
        log.info("Domain is ${domain}")
        scan_for_links(base, links, internal_links, dead_links, external_links, image_links, paths, levels)
        log.info "============ GOOD ======================"
        links.sort()
        for (link in links) {
            log.info link
        }
				
        log.info "============ BAD ======================"
        for (link in dead_links) {
            log.info "${link[0]} -> ${link[1]}"
        }
	
        log.info "============ LOCAL ======================"
        for (link in internal_links) {
            log.info "${link[0]} -> ${link[1]}"
        }
	
        log.info "============ IMAGES ===================="
        for (link in image_links) {
            log.info link
        }		
		
        log.info "============ EXTERNAL =================="
        for (link in external_links) {
            log.info "${link[0]} -> ${link[1]}"
        }		
		
        //log.info "============ PATHS ======================"
        //keys = paths.keys()
        //keys.sort()
        //for link in keys:
        //  log.info link
        //  for url in paths[link]:
        //    log.info "\t\t\t\t\t\t\t%s" % url
    }
    
    @RequestMapping("/")
    String index(Map<String, Object> model) {
        model.put("domain", "http://${domain}")
        model.put("links", links)
        "good"
    }

   @RequestMapping("/internal")
    String local(Map<String, Object> model) {
        model.put("domain", "http://${domain}")
        model.put("internal_links", internal_links)
        "internal"
    }
	
	
    @RequestMapping("/external")
    String external(Map<String, Object> model) {
        model.put("domain", "http://${domain}")
        model.put("external_links", external_links)
        "external"
    }

    @RequestMapping("/image")
    String image(Map<String, Object> model) {
        model.put("domain", "http://${domain}")
        model.put("image_links", image_links)
        "image"
    }
	
    @RequestMapping("/bad")
    String bad(Map<String, Object> model) {
        model.put("domain", "http://${domain}")
        model.put("dead_links", dead_links)
        "bad"
    }

    
    @RequestMapping(value="/update", method=RequestMethod.POST)
    void update() {
        run()
    }
    
}