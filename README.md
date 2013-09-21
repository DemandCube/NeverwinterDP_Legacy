NeverwinterDP
=============

Neverwinter DP - 


Problem - Data Collection

Neverwinter - The real-time log/data pipeline. The Nginx, Kafka, Hadoop and Storm Data pipeline with Logstash, Cacti and Ganglia

Providing
- High Availability and Scalable Data Collection
- Framework and Data Monitoring
- with ZeroConf

High Availability and Performant  Log Collection
- Log Distribution - Multi-data center
- Log Replay
- Log Monitoring
- Log Search
- Log Operational Watchdog
- Log Reporting
- Log Alerting

[[https://raw.github.com/DemandCube/NeverwinterDP/master/diagrams/images/NWD-HighLevel.png]]

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

- Nginx -> Kafka
- Nginx -> Logrotate
- Nginx -> module timestamp
- Nginx -> logrotation module
- Logrotate frequency mod
- Logtail - find reference to old project that I looked at
- Filehandle monitor

Capabilities
- file handle monitor
- file process monitor
- file tail
- Kafka efficient socket to file transfer
