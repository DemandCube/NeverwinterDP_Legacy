NeverwinterDP
=============

Neverwinter DP - 


Problem - Data Collection

Neverwinter - The real-time log/data pipeline. The Nginx, Kafka, Hadoop and Storm Data pipeline with Logstash, Cacti and Ganglia


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

![NWD-HighLevel](https://raw.github.com/DemandCube/NeverwinterDP/master/diagrams/images/NWD-HighLevel.png "A Highlevel Diagram")
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

TODO
====
- [ ] Log Stash to Neverwinter Plugin
- [ ] Log Collection Standard
- [ ] Neverwinter Nginx Plugin to Kafka
- [ ] Neverwinter Kafka Queue Monitor in Kibana
- [ ] Develop NW Distributed Data Pump -> HCatalog - Think can be distributed framework for managing a cluster of writers to Flume or Logstash

QUESTIONS
=========
Should a distributed fault tolerant data transport layer from Kafka to hadoop be build on 
- 1) Storm
- 2) Yarn



[ Front End Emmiter ]
- The concept is to have a nginx front end that will ship logs to 
- 

[rest end point (nginx) ] ->  [ data bus (kafka) ] -> [ data transport/connector (storm or yarn) ] -> [ rdbms (hive - data registration live) |  file system (hdfs)  | key store (hbase) ]

