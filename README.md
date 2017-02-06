# CF Web Crawler

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
![image](https://cloud.githubusercontent.com/assets/3868736/22636824/f3e553c0-ec0b-11e6-878a-2e8e862b83aa.png)

You can can restrict search by specifying a level
```
> spring run crawler.groovy -- --domain=google.com --depth=1
```
This will only navigate down one level. Any links below that will not be visited.

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
2017-02-06T01:07:08.68-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:08.683  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/25/donald-trump-immigration-border-mexico-wall-ex

Increase the number of app instances from one to two:ecutive-order-zeleny-ctn.cnn
2017-02-06T01:07:09.23-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:09.231  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/14/jeff-jeans-obamacare-saved-life-didnt-understand-paul-ryan-sot-ctn.cnn
2017-02-06T01:07:09.73-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:09.731  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/tv/2017/01/05/donald-trump-julian-assange-james-woolsey-panel-ctn.cnn
2017-02-06T01:07:10.14-0500 [APP/PROC/WEB/0]OUT 2017-02-06 06:07:10.146  INFO 51 --- [       runner-0] com.shozab.Crawler                       : Crawling http://cnn.com/videos/politics/2017/01/04/donald-trump-intelligence-briefing-russian-hack-vo-ctn.cnn
```

### High Availability
You can assure high avaialabilty by scaling the application. You can scale the application in several ways; for example you can increase the number instances of the application. Cloud Foundry will assure there are at least X number of instances running of the CF WebCrawler. If one of the instances goes down, Cloud Foundry will automatically start up and another instance, rebalancing the load. 

#### How to Scale Up
Increasing the available disk space or memory can improve overall app performance. Similarly, running additional instances of the app can allow cf webcrawler to handle increases in user load and concurrent requests. These adjustments are called scaling.

Scaling the app horizontally adds or removes app instances. Adding more instances allows the application to handle increased traffic and demand.

Increase the number of app instances from one to two:
```
> cf scale cf-webcrawler 2
```

Check the status of the app and verify there are two instances running:
```
> cf app cf-webcrawler 
```
Output: 
```
requested state: started
instances: 2/2
usage: 512M x 2 instances
urls: cf-webcrawler-anomic-ea.cfapps.io
last uploaded: Mon Feb 6 06:03:13 UTC 2017#
stack: cflinuxfs2
buildpack: container-certificate-trust-store=1.0.0_RELEASE java-buildpack=v3.12-offline-https://github.com/cloudfoundry/java-buildpack.git#6f25b7e java-opts open-jdk-like-jre=1.8.0_121 open-jdk-like-memory-calculator=2.0.2_RELEASE spring-boot-cli=1.4.3_RELEASE

     state     since                    cpu      memory           disk           details
#0   running   2017-02-06 01:37:59 AM   4.4%     368.5M of 512M   152.8M of 1G
#1   running   2017-02-06 01:42:07 AM   227.8%   354.8M of 512M   152.8M of 1G
```

You can also scale vertically. Scaling the app vertically changes the disk space limit or memory limit for each app instance.
```
> cf scale cf-spring -m 1G
```
```
> cf scale cf-spring -k 512M
```


# Continuous Delivery
In order to reduce operation overheard, automation is important. Demo is available upon request. 

## Automating BUILD, TEST, and DEPLOYMENT
![image](https://cloud.githubusercontent.com/assets/3868736/22637399/9fc27c88-ec0f-11e6-8624-e81f2726d582.png)

Continuous Delivery is the automation of all the steps from code check all the way to deployment and delivery. Automation significantly reduces bugs found in production.

### Set up your GitHub Repository
### Configure Jenkins to monitor code pushes to GitHub and Deploy to Cloud Foundry
**Prerequisites**
- Jenkins 
- Git plugin
- Cloud Foundry plugin

**Step 1: Create Project in Jenkins**
- 1. Create a free style project
- 2. Under "Source Code Management" tick "Git" and complete required information
- 3. Under "Post Build Actions" select "Push to Cloud Foundry" and complete required information

**Step 2: GitHub**
- 1. Login to the your GitHub repo
- 2. Goto settings > integration & services and add "Add Jenkins (Git plugin)"

Now when ever you push code from your local machine to your remote repository on GitHub, Jenkins will automatically build the source and deploy it on to Cloud Foundry. 


**Step 3: Integrate Test Automations Suites with Jenkins**
- Example [Jenkins + Selenium Integration](http://learn-automation.com/selenium-integration-with-jenkins/)

![image](https://cloud.githubusercontent.com/assets/3868736/22637541/5e301202-ec10-11e6-9194-a8266592576d.png)


## Authors

* **Shozab Naqvi** - *Initial work* - [PurpleBooth](https://github.com/naqsh01)

## Acknowledgments

* Application source is based on top of gregturn/spring-crawler
* jSoup library


