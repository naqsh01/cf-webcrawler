# Cloud Foundry Web Crawler

Highly scalable and always available Spring based web crawler deployed on the Cloud Foundry PaaS. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
- Java 6 or above
- JAVA_HOME environment variable should be set to a JDK
- Spring Boot Command Line Interface
- A free [Pivotal Web Services](https://try.run.pivotal.io/gettingstarted) account
- Cloud Foundry Command Line Interface
- Git CLI

### Installing

A step by step series of examples that tell you have to get a development env running

**Clone repository locally**

```
> git clone https://github.com/naqsh01/cf-webcrawler.git
```

### How to Run Locally
```
> spring run crawler.groovy -- --domain=google.com
```
The service will begin to crawl google.com and categorize the links. Port 8080 must be available on the localhost. Open your browser to: 

```
http://localhost:8080
```

## Run on Pivotal Cloud Foundry
Once you've verified the source builds and runs properly locally it's time to push it on Cloud Foundry. Cloud Foundry is an open source cloud platform as a service (PaaS) on which developers can build, deploy, run and scale applications on public and private cloud models.

### Deploy to Cloud Foundry
**The Manifest File**

Application manifests tell [cf push] what to do with applications. This includes everything from how many instances to create and how much memory to allocate to what services applications should use.

A manifest can help you automate deployment, especially of multiple applications at once.
```
---
applications:
- name: cf-webcrawler
  memory: 512M
  instances: 1
  random-route: true
  env:
    JAVA_OPTS: '-Ddomain=cnn.com'
```
The manifest above tells CF to create an application called "cf-webcrawler" with 512M of memory allocated towards it and run 1 instance of the app. It also tells CF to set the environment variable JAVA_OPTS to "-Ddomain=google.com."

Navigate to the app directory:
```
> cd cf-webcrawler
```
Sign in to PWS:
```
> cf login -a https://api.run.pivotal.io
```
Push the app to PWS:
```
> cf push 
```
Open the sample app in your browser:
```
[...]
requested state: started
instances: 1/1
usage: 512M x 1 instances
urls: cf-webcrawler-anomic-ea.cfapps.io
last uploaded: Mon Feb 6 01:40:15 UTC 2017
stack: cflinuxfs2
buildpack: container-certificate-trust-store=1.0.0_RELEASE java-buildpack=v3.12-offline-https://github.com/cloudfoundry/java-buildpack.git#6f25b7e java-opts open-jdk-like-jre=1.8.0_121 open-jdk-like-memory-calculator=2.0.2_RELEASE spring-boot-cli=1.4.3_RELEASE

     state     since                    cpu      memory           disk           details
#0   running   2017-02-05 08:41:09 PM   190.7%   365.3M of 512M   152.8M of 1G
```
Go to the url:
```
urls: cf-webcrawler-anomic-ea.cfapps.io
```

### View Logs
Cloud Foundry provides access to an aggregated view of logs. This includes HTTP access logs, as well as output from app operations such as scaling, restarting, and restaging.

View a snapshot of recent logs:
```
> cf logs cf-webcrawler --recent
```
Or, stream live logs:
```
> cf logs cf-webcrawler 
```

Output:
```
2017-02-06T01:07:06.80-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:06.805  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/shows/cnn-tonight
2017-02-06T01:07:07.94-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:07.948  INFO 51 --- [       runner-0] com.shozab.Crawler                       : http://facebook.com/CNNTonight10 leaves this domain, so not crawling
2017-02-06T01:07:07.94-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:07.948  INFO 51 --- [       runner-0] com.shozab.Crawler                       : http://twitter.com/CNNTonight leaves this domain, so not crawling
2017-02-06T01:07:07.94-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:07.948  INFO 51 --- [       runner-0] com.shozab.Crawler                       : http://www.instagram.com/cnntonight leaves this domain, so not crawling
2017-02-06T01:07:07.94-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:07.948  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/us/2017/01/24/minnesota-governor-mark-dayton-collapses-vo-lemon-ctn.cnn
2017-02-06T01:07:08.39-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:08.397  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/world/2017/01/26/mexico-president-will-not-pay-for-wall-santiago-ctn.cnn
2017-02-06T01:07:08.68-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:08.683  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/25/donald-trump-immigration-border-mexico-wall-executive-order-zeleny-ctn.cnn
2017-02-06T01:07:09.23-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:09.231  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/14/jeff-jeans-obamacare-saved-life-didnt-understand-paul-ryan-sot-ctn.cnn
2017-02-06T01:07:09.73-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:09.731  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/tv/2017/01/05/donald-trump-julian-assange-james-woolsey-panel-ctn.cnn
2017-02-06T01:07:10.14-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:10.146  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/04/donald-trump-intelligence-briefing-russian-hack-vo-ctn.cnn
```



## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```
```
```


### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

