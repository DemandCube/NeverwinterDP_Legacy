NeverwinterDP - NDP
=============
- A [DemandCube](https://github.com/DemandCube) Project

NeverwinterDP (NDP) the Lambda Architecture based Big Data Pipeline for Hadoop and Data Systems, designed for operational reliability at the scale of billions of events.

Community
======
  [Join us] (https://github.com/DemandCube/NeverwinterDP/blob/master/README.md#join-us-if) if you have [Grit] (http://www.ted.com/talks/angela_lee_duckworth_the_key_to_success_grit)

- [Join the Developer Mailing List](https://groups.google.com/forum/#!forum/neverwinterdp)
  - This list is for people who want to contribute code to NeverwinterDP.
- [Join the User Mailing List](https://groups.google.com/forum/#!forum/neverwinterdp-user)
  - This list is for usage questions, help, and announcements. 
- IRC channel #neverwinterdp on irc.freenode.net
- [Google Hangout] (http://www.google.com/hangouts/)
  - It's what commiters use to chat directly but have to connect with us first on the mailing list, then request an invite there.
- [List of Issues] (https://github.com/DemandCube/NeverwinterDP/issues?labels=&state=open)
- [Kanban Board of Issues] (https://huboard.com/DemandCube/NeverwinterDP/#/)
- [Repo Listing] (http://demandcube.github.io/)

STATUS
======
 Alpha - Currently the project is under active core development and not ready for production.


WHAT IS NEVERWINTER DP?
=======================
NeverwinterDP is a distributed, reliable, and available service for efficiently collecting, aggregating, and moving large amounts of log/event data.

Neverwinter is an open source distributed data ingestion system/framework for capturing large amounts of data  (ranging from gigabytes to petabytes) to be (processed or saved in real-time) to one or more down databases / repositories (i.e. Hadoop, HDFS, S3, Mysql, Hbase, Storm).

Neverwinter was designed and written from the ground up for reliability, scalability, and operational maintainability to support the growing needs of event and message data collection at scale to support startups and enterprise organizations.

Neverwinter - The real-time log/data pipeline. Sparkngin(Nginx) Kafka, Scribengin, leveraging processing in Hadoop and Storm the Data pipeline integrates with Logstash, Ganglia and Nagios Integration.  It's a replacement for flume but also can be integrated with it.


It Supports:

1. Batch Analytics
2. Realtime Analytics (Storm and Spark-Streaming)

Neverwinter is the combination of three major open source project that leverage the best in open source. 

1. [Sparkngin](https://github.com/DemandCube/Sparkngin) - (powered by Netty)
2. [Queuengin](https://github.com/DemandCube/Queuengin) - (powered by Kafka or Kinesis)
3. [Scribengin](https://github.com/DemandCube/Scribengin) - (powered by Yarn)

Now that we have used enough buzz words.  Neverwinter reliably captures lots of data and saves it to hadoop and other systems.

WHAT CAN NEVERWINTERDP DO?
========================
Neverwinter allows data ingestion from any system that can emit http/rest (or other protocols) calls and then publish this data to a down stream database, including Hive, HBase, relational databases or even proprietary data stores. A single Neverwinter pipeline can combine data from multiple sources and deliver them to multiple sources, allowing for data to be delivered to multiple team or an entire organization.

Neverwinter is targeted at data and analytics engineering teams who expect response times ranging from sub-second to minutes. Neverwinter breaks the false choice between having  a batch or real-time system. Also the false choice between having a fast or maintainable system.

WHO SHOULD USE NDP?
========================
* Gaming industry
* Media Companies
* Publisher
* Telecom
* Car Companies
* Utilities

Goals
---------
- Provide Automatic Failover
- Reliable data delivery under single node failures in any tier
- Horizontal Scalability
- Ability to reliably work in a environment running [Chaos Monkey] (https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey) and the [Simian Army] (https://github.com/Netflix/SimianArmy/wiki)
- Easy operational maintainability
- High performance and low cost pipeline
- Data flow connectors to multiple destination systems (HDFS, HBase, Oracle, Teradata, Netteza) 
- Symantics that support back pressure on extreme load 

Use-Cases / Examples
---------
- [Twitch's Data Pipeline](http://ossareh.posthaven.com/the-twitch-statistics-pipeline)
- [Linkedin's Data Pipeline](http://sites.computer.org/debull/A12june/pipeline.pdf)
- [Pod Cast](http://wp.me/pTu1i-b5)
- [Metamarkest Data Pipeline](https://metamarkets.com/2014/building-a-data-pipeline-that-handles-billions-of-events-in-real-time/?utm_source=twitterfeed&utm_medium=twitter)
- [Addthis Data Pipeline](http://www.addthis.com/blog/2013/04/16/building-a-distributed-system-with-akka-remote-actors/#.U87OAYBdXR0)
- Usertracking and Profiling
- Ad data tracking
- System Event Capture
- Game activity data
- Mobile App User Behavior

Components
----------

1) Http/Rest/ZeroMQ Log Collection Endpoint - Sparkngin
- [Sparkngin](https://github.com/DemandCube/Sparkngin) - powered by [Netty](http://netty.io/)

2) Data Bus
- [Queuengin](https://github.com/DemandCube/Queuengin) - powered by ([Kafka](http://kafka.apache.org/) or [Amazon Kinesis](http://aws.amazon.com/kinesis/))

3) Data Pump/Transport
- [Scribengin](https://github.com/DemandCube/Scribengin) - powered by   [Yarn](http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/YARN.html)

Related Projects
----------
1. [vagrant-flow](https://github.com/DemandCube/vagrant-flow) Vagrant Plugin allows for a better ansible flow also generates ansible inventory files, and runs playbooks
1. [ansible-flow](https://github.com/DemandCube/ansible-flow) Ansible modules to make working with ansible easier
2. [DeveloperPlayBooks](https://github.com/DemandCube/DeveloperPlayBooks) Ansible Playbooks to quickly setup and install DemandCube projects
3. [DemandSpike](https://github.com/DemandCube/DemandSpike) Load Testing Framework for Distributed Applications
4. [KafkaSphere](https://github.com/DemandCube/KafkaSphere) Web console for Kafka that has a Web tier that talks to a REST Api tier

Copy all Repositories
----------
`curl -s https://api.github.com/orgs/DemandCube/repos | ruby -rubygems -e "require 'json'; JSON.load(STDIN.read).each {|repo| %x[git clone #{repo['ssh_url']} ]}"`

Architecture
------------
1. High Level

```
    +-----------+    +-------------+    +---------------+    +------------+    +------------+
    |Source     |    |Rest         |    |Persistent     |    |Data        |    |Data        |
    | Client    |+-->| Endpoint    |+-->| Queue/Buffer  |+-->| Distributor|+-->| Sink       |
    |           |    |(Sparkngin)  |    |(Kafka/Kinesis)|    |(Scribengin)|    |(Hive/Hbase)|
    +-----------+    +-------------+    +---------------+    +------------+    +------------+
```
2. Mid Level
  1. Source Client
  2. Collector Endpoint
  3. Collector Producer
  4. Persistent Queue/Buffer
  5. Data Distributor / Stream Processor (CEP - Complex Event Processing)
  6. Data Sink

Transport Protocol Levels
--------
1. Binary
2. Framework
3. Schema
4. Encryption

<dl>
  <dt>Binary</dt>
  <dd>This level transports any arbitrary data</dd>
  <dt>Framework</dt>
  <dd>This level transports any data wrapped in fields of data needed by the framework for monitorying</dd>
  <dt>Schema</dt>
  <dd>This level adds schemas to the data being transported in the framework layer</dd>
  <dt>Encryption</dt>
  <dd>This level adds encrytion around the data in schemas or the framework transport</dd>
</dl>



![NeverwinterDP Architecture](diagrams/images/NeverwinterDP-Abstract-v1.png?raw=true "NeverwinterDP Architecture")
![NeverwinterDP Architecture with Use-cases](diagrams/images/NeverwinterDP-Conceptual-v1.png?raw=true "NeverwinterDP Architecture with Use-cases")
![NeverwinterDP Architecture with concrete integrations](diagrams/images/NeverwinterDP-Concrete-v1.png?raw=true "NeverwinterDP Architecture with concrete integrations")

Yarn
=====
- [What is Yarn] (http://hortonworks.com/hadoop/yarn/)
- [Introducing Yarn Blog Series] (http://hortonworks.com/blog/introducing-apache-hadoop-yarn/)
- [Getting started with Yarn] (http://hortonworks.com/get-started/yarn/)
- [Running Applications on Yarn] (http://hortonworks.com/blog/running-existing-applications-on-hadoop-2-yarn/)
- [Dependency Management] (http://hortonworks.com/blog/management-of-application-dependencies-in-yarn/)
- [Yarn Issues] (http://issues.apache.org/jira/browse/YARN)
- [Yarn Mailing list] (http://hadoop.apache.org/mailing_lists.html#YARN)
- [Yarn Book] (http://my.safaribooksonline.com/book/databases/hadoop/9780133441925)
- [Slider Project] (https://wiki.apache.org/incubator/SliderProposal)
- [Slider Codebase] (https://github.com/hortonworks/slider)
- Issue - [Support Docker containers in YARN] (https://issues.apache.org/jira/browse/YARN-1964)

Join us if
======
- You have **[Grit] (http://en.wikipedia.org/wiki/Grit_(personality_trait))** like us. **perseverance and passion for long-term goals**
- You have **Grit** and know what [Grit Means] (http://www.fastcompany.com/1722712/why-true-grit-matters-face-adversity)
- You have a passion of great engineering
- You love data systems engineering
- You love the challenge of building highly scalable highly avaliable systems that just work
- You want to make a difference
- [You want to code every day] (http://ejohn.org/blog/write-code-every-day/)

Then make friends with us on the mailing list and start by making a contribution and solving a [Issue] ()

How to Contribute
======

There are many ways you can contribute towards the project. A few of these are:

**Jump in on discussions**: It is possible that someone initiates a thread on the [Mailing List](https://groups.google.com/forum/#!forum/neverwinterdp) describing a problem that you have dealt with in the past. You can help the project by chiming in on that thread and guiding that user to overcome or workaround that problem or limitation.

**File Bugs**: If you notice a problem and are sure it is a bug, then go ahead and file a [GitHub Issue](https://github.com/DemandCube/NeverwinterDP/issues?state=open). If however, you are not very sure that it is a bug, you should first confirm it by discussing it on the [Mailing List](https://groups.google.com/forum/#!forum/neverwinterdp).

**Review Code**: If you see that a [GitHub Issue](https://github.com/DemandCube/NeverwinterDP/issues?state=open) has a "patch available" status, go ahead and review it. The other way is to review code submited with a [pull request](https://help.github.com/articles/using-pull-requests), it is the prefered way.  It cannot be stressed enough that you must be kind in your review and explain the rationale for your feedback and suggestions. Also note that not all review feedback is accepted - often times it is a compromise between the contributor and reviewer. If you are happy with the change and do not spot any major issues, then +1 it.

**Provide Patches**: We encourage you to assign the relevant [GitHub Issue](https://github.com/DemandCube/NeverwinterDP/issues?state=open) to yourself and supply a patch or [pull request](https://help.github.com/articles/using-pull-requests) for it. The patch you provide can be code, documentation, tests, configs, build changes, or any combination of these.


Workflow
======


1. Create issue on NeverwinterDP for the work (linking only be convention)
2. Announce issue on mailinglist and discuss design on the mailinglist
3. Sign CLA if you haven't yet - request from neema ( at ) demandcube.com 
4. Do development according Git Workflow Summary
5. Request Code Review on NeverwinterDP mailinglist e.g. "[Code] Review Request Issue #111: Title" Followed by link to pull request.
  - Additionally include a short summary of what feature/bug/enhancement this pull request addresses.
  - put a link to the pull request in the issue
  - label the issue with "* needs a code review *"
6. A Commiter reviews the issue, and changes in the pull request (Accepting or requesting changes first)

(Remember to update Kanban during this process)

**Git Workflow Summary**

1. **Fork the Repository or Update Fork**
1. **Create a branch for your feature development - off of master or appropriate branch **
1. **Do your Development**
1. **Stay in your feature branch**
1. **Squash commit on your feature branch (Optional)**
1. **Update local master from upstream repository**
1. **Merge from feature branch onto your local master branch**
1. **Issue pull request on github**
1. **Stop making changes on the master branch till merged**

If you have a issue that needs a code review:

  1. Announce it on the mailing list according to 
  2. label the issue with "* needs a code review *"
  3. put a link to the pull request in the issue

If you did a code review, your requested changes from the submitter have been fixed then:

  1. remove the label "* needs a code review *"
  1. label the issue with "* ready to merge *"
  1. comment on the ticket "ready-to-merge:@yourgithubusername"
  1. If your a commiter then merge in the changes


Git Workflow to Develop a new Feature
====

- Step 1(New Fork): 
  - # Hit the "Fork" Icon in Github.com web site when on the Repository Page e.g. "DemandCube/NeverwinterDP"

- Step 1(Existing Fork e.g. "YourUserName/NeverwinterDP"):
  - # pull in changes from the master upstream/parent repo
  - # No ff is the way you should since it won't automatically commit and you can comment the merge commit
  - `git pull --no-ff https://github.com/DemandCube/NeverwinterDP.git master`
  - # Now fix any merge conflicts if they exist
   
- Step 2:
  - # Create a branch todo your development "feature/featurename"
  - `git checkout -b feature/featurename master`

- Step 3: 
  - # Do any development that you need todo

- Step 4 (Optional - but recommended):
  - # make sure your still in the feature branch
  - `git checkout feature/featurename`
  - # squash commits in the feature/featurename when your done and ready to commit
  - # This is in Repo => "YourUserName/NeverwinterDP" on branch "feature/featurename"
  - # Assumes your branch is made from "master"
  - `git rebase -i master`

- Step 5:
  - # Now merge updates into the master
  - `git checkout master`
  - `git pull --no-ff https://github.com/DemandCube/NeverwinterDP.git master`
  - # resolve any dependency conflicts
  
- Step 6:
  - # Now merge your feature into your local master
  - `git checkout master`
  - `git merge feature/featurename`
  - # Push to github
  - `git push origin master`

- Step 7:
  - # Issue pull request on Github
    -  Request Code Review on NeverwinterDP mailinglist e.g. "Review Request Issue #111: Title" Followed by link to pull request.
    - include a short summary of what feature/bug/enhancement this pull request addresses.
    - put a link to the pull request in the issue
    - label the issue with "* needs a code review *"

Keep your fork updated
====
[Github Fork a Repo Help](https://help.github.com/articles/fork-a-repo)


- Add the remote, call it "upstream":

```
git remote add upstream git@github.com:DemandCube/NeverwinterDP.git
```
- Fetch all the branches of that remote into remote-tracking branches,
- such as upstream/master:

```
git fetch upstream
```
- Make sure that you're on your master branch:

```
git checkout master
```
- Merge upstream changes to your master branch

```
git merge upstream/master
```



How to Submit - Patches/Code
======

1. **Create a patch**
  * Make sure it applies cleanly against trunk
1. **Test**
  * If code supply tests and unit test
1. **Propose New Features or API**
  * Document the new Feature or API in the Wiki, the get consensus by discussing on the mailing list
1. **Open a GitHub Ticket**
  * Create the patch or pull request, attach your patch or pull request to the Issue.
    * Your changes should be well-formated, readable and lots of comments
    * Add tests
    * Add to documentation, especially if it changes configs
    * Add documentation so developers, can understand the feature or API to continue to contribute
  * Document information about the issue and approach you took to fix it and put it in the issue.
  * Send a message on the mailing list requesting a commiter review it.
  * Nag the list if we (commiters) don't review it and followup with us.

1. **How to create a patch file**: 
  * The preferred naming convention for Sparkngin patches is `SPARKNGIN-12345-0.patch` where `12345` is the Issue number and `0` is the version of the patch. 
  * Patch Command:
    * `$ git diff > /path/to/SPARKNGIN-1234-0.patch`

1. **How to apply someone else's patch file**: 
```
$ cd ~/src/Sparkngin # or wherever you keep the root of your Sparkngin source tree 
$ patch -p1 < SPARKNGIN-1234-0.patch # Default when using git diff
$ patch -p0 < SPARKNGIN-1234-0.patch # When using git diff --no-prefix
```

1. Reviewing Patches
  * [Find issues with label "Patch Available"](https://github.com/DemandCube/Sparkngin/issues?labels=patch+avaliable&page=1&state=open), look over and give your feedback in the issue as necessary.  If there are questions discuss in the [Mailing List](https://groups.google.com/forum/#!forum/sparkngin).


1. Pull Request
  - Issue pull request
    - https://help.github.com/articles/merging-a-pull-request
  - Announce on the mailing list and request code review


How to Build NeverwinterDP
======
```
mkdir workspace
cd workspace
git clone https://github.com/DemandCube/NeverwinterDP-Commons
git clone https://github.com/DemandCube/Queuengin
git clone https://github.com/DemandCube/Sparkngin
git clone https://github.com/DemandCube/Scribengin
git clone https://github.com/DemandCube/Demandspike
git clone https://github.com/DemandCube/NeverwinterDP

cd NeverwinterDP-Commons
gradle clean build install

cd ../Queuengin
gradle clean build install

cd ../Sparkngin
gradle clean build install

cd ../Scribengin
gradle clean build install

cd ../Demandspike
gradle clean build install

cd ../NeverwinterDP
gradle clean build install release

cd  build/release/NeverwinterDP

#To launch servers, you have two choices - single node server or multi node server
./bin/local-single-jvm-server.sh 
#or  
./bin/local-multi-jvm-server.sh to launch the servers

#At this point, we need to wait for the servers to come up
#Make sure that there are 9 server are RUNNING before you run local-test.js by running this step
./bin/shell.sh -c server ping

#Run the script to deploy the services. This script will install kafka, sparkngin, demandspike ... services on the servers with the conresponding role
./bin/jsrun.sh  jscript/local-deploy.js

#At this point you can point your browser to this url to see status
http://localhost:8080/app/index.html

#Run the script to deploy some demandspike job to demandspike scheduler service
#Those 2 commands will submit various kafka and sparkngin demandspike job test to a job scheduler
#Go to the webui , click the DemandSpike then Job Scheduler to monitor the test results.
./bin/jsrun.sh  jscript/local-kafka-test.js
./bin/jsrun.sh  jscript/local-sparkngin-test.js

#To run the single job:
./bin/jsrun.sh  jscript/ringbearer/job/kafka/hello-job.js

#To kill the servers
./bin/shell.sh -c server exit
#or
pkill -9 -f neverwinter


In Queuengin , Scribengin, DemandSpike. You can run gradle release after build and install, you will find the release in build/release/project directory 
and run some test in each release. For example in Queuengin,

cd build/release/Queuengin/bin
#To launch the server
./server.sh 
#Ping to check the server status
./shell.sh -c server ping 
#To launch the batch script tets
./shell.sh -f  hello-xyz.csh 

```

Known Problems
=======
There are known problems:

1. make sure you run gradle clean install and no test fail.  Check  ps -a  (ps -aux on linux) | grep java 
to make sure all the jvm thread exit properly after each build. There can be problem that prevent the jvm 
shutdown properly and it will cause the problem for the next build or test.
2. If you run multi jvm env,  you will have a log file for each server or jvm instances, a single log file if you use a single jvm.
3. For DemandSpike job I run a local job runner that simulate the yarn app , so you won't see or need  yarn + hadoop log.
4. A suceccessful jscript/local-test.js should produce some kafka metric on demandspike and kafka server.
5. There are lot of elastic search exception in the log due the unavaibility of elasticsearch server  at the beggining. 
You can remove elastic search log appender in config/log4j.properties
6. Too many open files probem.  If you run multi jvm env you may run into too many open files problem  check 
this for the solution http://superuser.com/questions/433746/is-there-a-fix-for-the-too-many-open-files-in-system-error-on-os-x-10-7-1. 
If you have the problem  on linux you can update the /etc/security/limits.conf and add:
tuan            soft    nofile          16384
tuan            hard    nofile          16384
tuan            soft    nproc           1024
tuan            hard    nproc           1024



## Github Help
  * [How push from your local repo to github](https://help.github.com/articles/pushing-to-a-remote#pushing-a-branch)
  * [How to send a pull request](https://help.github.com/articles/using-pull-requests)
  * [How to sync a forked repo on github](https://help.github.com/articles/syncing-a-fork)
  * [Other Suggested Git Workflows](https://cwiki.apache.org/confluence/display/KAFKA/Git+Workflow)
  * [How to Branch](https://github.com/Kunena/Kunena-Forum/wiki/Create-a-new-branch-with-git-and-manage-branches)

## Kanban Board
If you can't actually move issues around let me (Steve) know.

- "Accepted" - are tickets you plan to start working on this week.
- "Working on" - are tickets your actively working on
- "In code review" - are tickets that need a code review ( You should have put a code review request on the mailinglist ) (If no one responded it's up to you to followup)
- "Working on documentation and automated tests" - are tickets your finishing the documentation and creating, unit, integration, configuration management/deployment (Ansible) installation tests.
- "In documentation and automated test review" - review specifically of the documentation and test.  Follows the same process as code reviews.  A review should be requested on the mailinglist.
- "Done" - The task should pass the automated integration test review from Jenkins

* * *

### Tools-Core:
- ansible
- gradle

### Team Tools:
- Github
- Huboard
- github-flow
- ansible
- vagrant

### Supported Servers:
- RHEL/CentOS
- Ubuntu

### Main Programming Languages:
- Java
- Python
- Javascript
- C

## Other Languages:
- Ruby

* * * 

Other
======

HA Testing
 - Testing using [SimianArmy](https://github.com/Netflix/SimianArmy/wiki) and [Chaos Monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Home), and [Jenkins](http://jenkins-ci.org/)

Providing
- Data Steaming/Collection Framework
- High Availability and Scalable Data Collection
- Data Monitoring
- Autoconfiguration - with ZeroConf - Stored in Zookeeper
- Data Partition Notification


Additional Features: High Availability and Performant  Log Collection
- Log Distribution - Multi-data center
- Log Replay
- Log Monitoring
- Log Search
- Log Operational Watchdog
- Log Reporting
- Log Alerting

Logs are fed into
- HDFS
- Elastic Search
- Hive
- Mysql
- HBase
- Vertica

TODO
====
- [ ] Put in contributor information and update projects to reference (Kafka and Flume)
- [ ] Log Stash to Neverwinter Plugin
- [ ] Log Collection Standard
- [ ] Neverwinter Nginx Plugin to Kafka
- [ ] Neverwinter Kafka Queue Monitor in Kibana
- [ ] Develop NW Distributed Data Pump -> HCatalog - Think can be distributed framework for managing a cluster of writers to Flume or Logstash
- [ ] Add other main github projects

Areas to flush out
====
Prototype framework with zmq in python

Topics
- Registry
- Heartbeat
- Stats
- LogTopics

- [ ] Develop - Protocol

Main development
====
- Sparkngin
- Connector Component

Monitoring Outof the box
====
Out of the box super easy plugin to
- Nagios
- Ganglia

![NWD-HighLevel](diagrams/images/NWD-HighLevel.png?raw=true "A Highlevel Diagram")

[Nginx] -> Openresty, libkafka with spillover buffer, spillagent, window registration and monitoring
- Nginx -> Kafka -> Flume -> Hcatalog -> Hive

[Log]
- Epoch timestamp, ip, process and optional type and version

[Monitoring] Log normal, error, watchdog, normal spill, error spill, watchdog spill
- Type, Lines, Size per minute per process per server

[Concept/Abstraction]
_ Emmiter Client
 - Zero Conf - module to -> zookeeper
 - async
 - persistence
 - send 
 - spillover
 - spillover recovery agent
- LogEmitter
- LogFormat
- LogVersion
- LogType
- WatchDogEmitter
- WatchDog Register
- WatchDog Monitor and Alerts


[Reporting]
- Summary Report
- LogType Report
- Key Coverage Report
- Value Coverage Report

[Support]
- Data Processing Assesment
- Process management
- Annual Data Assessment

[Dependencies]
- [OpenResty](http://openresty.org/)
- [Nginx](http://nginx.com/)
- [Kafka](http://kafka.apache.org/)
- [Hadoop](http://hadoop.apache.org/)
- [Ganglia](http://ganglia.sourceforge.net/)
- [Cacti](http://www.cacti.net/)
- [ElasticSearch](http://www.elasticsearch.org/)
- [LogStash](http://logstash.net/)
- [HCatalog](http://hive.apache.org/hcatalog/)
- [Hive](http://hive.apache.org/)
- [Storm](http://storm-project.net/)
- [HBase](http://hbase.apache.org/)
- [Flume](http://flume.apache.org/)
- [Kibana](http://www.elasticsearch.org/overview/kibana/)
- [Doxygen](http://www.stack.nl/~dimitri/doxygen/index.html)
- [Zeromq](http://zeromq.org)


[ To investigate ]
- Nginx -> Kafka
- Nginx -> Logrotate
- Nginx -> module timestamp
- Nginx -> logrotation module
- Logrotate frequency mod
- Logtail - find reference to old project that I looked at
- Filehandle monitor
 - fuser
 - inotify-tools

Capabilities
- file handle monitor
- file process monitor
- file tail
- Kafka efficient socket to file transfer
 

```
  +------------+    +-----------+    +------------+    +----------------+
  |NW          |    |NW         |    |NW          |    |                |
  |            |    |           |    |            |    |                |
  |  Front End |    |  Data Bus |    |  Data Pump |    | End Point      |
  |   Emitter  |+-->|           |+-->|            |+-->|- HDFS          |
  |  - Http Get|    |           |    |            |    |- Elastic Search|
  |  - Json    |    |           |    |            |    |                |
  |  - Avro    |    |           |    |            |    |                |
  +------------+    +-----------+    +------------+    +----------------+



  +-----------+     +---------+     +-----+    +--------+
  | Log Stash |     |Sparkngin|     |Kafka|    |Hadoop  |
  |-----------|     |---------|     |-----|    |--------|
  |           |+--->|         |+--->|     |+-->|HCatalog|
  |           |     |         |     |     |    |HBase   |
  +-----------+     +---------+     +-----+    +--------+
                                       +
                                       |        +--------+
                                       +------->|Storm   |
                                                |--------|
                                                |        |
                                                |        |
                                                +--------+
  
  http://www.asciiflow.com/#Draw
```



QUESTIONS
=========
Should a distributed fault tolerant data transport layer from Kafka to hadoop be build on 
- 1) Storm
- 2) Yarn



[ Front End Emmiter ]
- The concept is to have a nginx front end that will ship logs to 
- 

[ log collection (logstash] -> [rest end point (nginx) ] ->  [ data bus (kafka) ] -> [ data pump/transport (storm or yarn) ] -> [ rdbms (hive - data registration live) |  file system (hdfs)  | key store (hbase) ]

Look at developing the protocol prototype with a Avro Producer using zmq and a Avro Consumer communicating through kafka.
-Version/Lineage,Heartbeat,Source,Header/Footer.  Take design aspects from [Camus](https://github.com/linkedin/camus) , must provide built monitoring.  There
needs to be a messaging (source timestamp, system timestamp) and a way to inspect where hour boundries exist on the queue.  Additionally need a way
to register servers and when they come on and offline for log registration.  

Should there be the ability for schema registration, so that schema's can be pushed to downstream?  

Should there be a mapping and general payload support.  Json support 

Should Avro / Thrift / Protobuff / HBase / Hive / Storm - Type Mappings be maintained?

Kafka Resources
=====
- https://github.com/criteo/kafka-ganglia
- Creating Author:  Maxime Brugidou <maxime.brugidou@gmail.com>
- Interested Potential Contributor: Andrew Otto <otto@wikimedia.org>


Four Level Rosetta Protocol Stack
=====
- Level 0 Raw
- Level 1 Envelop Framework (Leverage Avro/Protocol Buffers/Thrift)
- Level 2 Event Payload
- Level 3 Encrypted Payload
- Events configured to flow by topic and get partitioned by either server timestamp or application supplied
- Sparknginx in-memory encryption layer. 

Preferred Development Tools
- [Ansible] (http://www.ansibleworks.com/)
- [Vagrant] (http://www.vagrantup.com/)
- [Virtualbox] (https://www.virtualbox.org/)
- [Gradle] (http://www.gradle.org/)
- [YourKit] (http://www.yourkit.com)

Sponsors of NeverwinterDP Big Data Pipeline Open Source Project
======
![YourKit](sponsors/yourkit-logo.png?raw=true "YourKit Logo")

* * *

YourKit supports NeverwinterDP open source project with its full-featured Java Profiler
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
[YourKit Java Profiler] (http://www.yourkit.com/java/profiler/index.jsp) and
[YourKit .NET Profiler] (http://www.yourkit.com/.net/profiler/index.jsp).


* * *
- YOUR COMPANY LOGO

* * * 
About Your Company


![Analytic](https://img.ndp.demandcube.com/v1/DemandCube/NeverwinterDP/ "NeverwinterDP Analytics")

Tracking pixel: ![Tracking Pixel Service](http://107.170.53.15:7080/tracking/pixel?site=github.com&webpage=NeverwinterDP)
