NeverwinterDP
=============

Neverwinter Realtime Data Platform




Problem - Data Collection

Neverwinter - The real-time log/data pipeline. SparknginP,Nginx, Kafka, Hadoop and Storm Data pipeline with Logstash, Cacti and Ganglia

Neverwinter is the combination of three major open source project that leverage the best in open source.

1) Rest Log Collection Endpoint - Sparkngin
- [Sparkngin - Nginx](https://github.com/DemandCube/Sparkngin)

2) Data Bus
- [Kafka](http://kafka.apache.org/)




3) Data Pump/Transport
- ????

Providing
- High Availability and Scalable Data Collection
- Framework and Data Monitoring
- with ZeroConf - Stored in Zookeeper?

High Availability and Performant  Log Collection
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



  +-----------+     +-----+     +-----+    +--------+
  | Log Stash |     |Nginx|     |Kafka|    |Hadoop  |
  |-----------|     |-----|     |-----|    |--------|
  |           |+--->|     |+--->|     |+-->|HCatalog|
  |           |     |     |     |     |    |HBase   |
  +-----------+     +-----+     +-----+    +--------+
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


Two Level Protocol Stack
===== 
- Level 1 Generic Protocol Stack (Leverage Avro/Protocol Buffers/Thrift)
- Level 2 Framework Stack
- Events configured to flow by topic and get partitioned by either server timestamp or application supplied
- Sparknginx in-memory encryption layer. (Pro version)
