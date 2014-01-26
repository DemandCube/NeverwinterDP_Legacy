NeverwinterDP
=============
- A [DemandCube](https://github.com/DemandCube) Project

Neverwinter Realtime Distributed Data Pipeline





WHAT IS NEVERWINTER DP?
=======================
Neverwinter is an open source distributed data ingestion system/framework for capturing large amounts of data  (ranging from gigabytes to petabytes) to be (processed or saved in real-time) to one or more down databases / repositories (i.e. Hadoop, HDFS, S3, Mysql, Hbase, Storm)

Neverwinter was designed and written from the ground up for scalability, and operational maintainability to support the growing needs of event and message data collection at scale to support startups and enterprise organizations.

Neverwinter - The real-time log/data pipeline. Sparkngin(Nginx) Kafka, Scribengin, leveraging processing in Hadoop and Storm Data pipeline with Logstash, Ganglia and Nagios Integration

Neverwinter is the combination of three major open source project that leverage the best in open source. 

1. Sparkngin (Nginx)
2. Kafka
3. Scribengin

Now that we have used enough buzz words.  Neverwinter reliably captures lots of data and saves it to hadoop and other systems.

WHAT CAN NEVERWINTER DO?
========================
Neverwinter allows data ingestion from any system that can emit rest calls and then publish this data to a down stream database, including Hive, HBase, relational databases or even proprietary data stores. A single Neverwinter pipeline can combine data from multiple sources and deliver them to multiple sources, allowing for data to be delivered to multiple team or an entire organization.

Neverwinter is targeted at data and analytics engineering teams who expect response times ranging from sub-second to minutes. Neverwinter breaks the false choice between having  a batch or real-time system. Also the false choice between having a fast or maintainable system.

Use-Cases
---------
Problem - Data Collection and Saving

Components
----------

1) Rest Log Collection Endpoint - Sparkngin
- [Sparkngin - Nginx](https://github.com/DemandCube/Sparkngin)

2) Data Bus
- [Kafka](http://kafka.apache.org/) or [Amazon Kinesis](http://aws.amazon.com/kinesis/) (Version 2)

3) Data Pump/Transport
- [Scribengin](https://github.com/DemandCube/Scribengin)


Architecture
------------
1. High Level

```
    +-----------+    +-------------+    +---------------+    +------------+    +------------+
    |Submission |    |Rest         |    |Transport      |    |Streaming   |    |Data        |
    | Client    |+-->| Endpoint    |+-->| Queue         |+-->| Adapter    |+-->| Repository |
    |           |    |(Sparkngin)  |    |(Kafka/Kinesis)|    |(Scribengin)|    |(Hive/Hbase)|
    +-----------+    +-------------+    +---------------+    +------------+    +------------+
```
2. Mid Level
  1. Submission Client
  2. Endpoint Collector
  3. Collector Producer
  4. Transport Queue
  5. Stream Processor (CEP - Complex Event Processing)
  6. Destination Adapter

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


Providing
- Data Steaming/Collection Framework
- High Availability and Scalable Data Collection
- Data Monitoring
- Autoconfiguration - with ZeroConf - Stored in Zookeeper?
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

[Consulting]
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


Four Level Protocol Stack
=====
- Level 0 Raw
- Level 1 Envelop Framework (Leverage Avro/Protocol Buffers/Thrift)
- Level 2 Event Payload
- Level 3 Encrypted Payload
- Events configured to flow by topic and get partitioned by either server timestamp or application supplied
- Sparknginx in-memory encryption layer. 

Preferred Development Tools
- [Ansible] (http://www.ansibleworks.com/)
- [Vangrant] (http://www.vagrantup.com/)
- [Virtualbox] (https://www.virtualbox.org/)
- [Gradle] (http://www.gradle.org/)

Github Pages
-------------
- <http://24ways.org/2013/get-started-with-github-pages/>

Site Examples
-------------
- http://brew.sh/
- http://flask.pocoo.org/
- http://phantomjs.org/
- http://showterm.io/
- http://stedolan.github.io/jq/
- http://www.sparkjava.com/
- http://leiningen.org/
- http://vertx.io/
- http://nodejs.org/
- http://hyde.github.io/
- http://www.crossroads.io/
- Maybe use http://jekyllrb.com/
- http://graphite.wikidot.com/
- http://graphite.readthedocs.org/en/1.0/tools.html
- https://github.com/snowplow/snowplow/wiki


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

