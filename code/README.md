#Design#

##Overrall Design##

```
 +------------+
 |Rest Client |  
  +-----------+   +-------------+    +---------------+    +------------+    +------------+
          |       |Rest         |    |Persistent     |    |Data        |    |Data        |
          +-----> | Endpoint    |+-->| Queuengin     |+-->| Distributor|+-->| Sink       |
          |       |(Sparkngin)  |    |(Kafka/Kinesis)|    |(Scribengin)|    |(Hive/Hbase)|
 +------------+   +-------------+    +---------------+    +------------+    +------------+
 |Demandspike |
 +------------+
```

##Message and Message Service##

From the overral design, we can consider each engine(client, Sparkngin, Scribengin, Data Sink) as a message service where the message is forwarded to each service , process and then forward to the next service point. At each service point, the service can reject the message due to the message error, save the message to retry later due to the next service point is not available

**The message structure**

The message structure should:

  - The message should be generic and hold any type of event/object
  - The message should be able to hold the log of the activities such:
      + client send the message to http server. 
      + http server receive the message.
      + http server forward the message to kafka queue
      + scribe engine dequeue the message
      + scribe engine write message to hbase.
        ....
  - The message should be able to hold a list of instructions so each service point can pick up the instruction and execute the instruction before it processes the message. For example the http service can pick up an instruction and drop the message to generate a failed acknowledge and force the client to retry the message.

```
  public class Message<T> {
    private String            key ;
    private byte[]            data;
    private List<MessageLog>         logs;
    private List<MessageInstruction> instructions;
  }

  public class MessageLog {
    private String host ;
    private String serviceId ;
    private long   time ;
    private String message ;
  }

  public class MessageInstruction {
    private String targetService ;
    private String instruction ;
    private Map<String, String> params ;
  }

```

The message service consists of 2 main component , the MessageService itself and the MessageServicePlugin


```

  public interface MessageService {
    String getName() ;
    void   setName(String name);

    void onInit(JSONPObject config) ;
    void onDestroy() ;

    void process(Message<?> message) ;
  }


  public interface MessageServicePlugin {
    String getName() ;
    void   setName(String name) ;

    void onPreProcess(MessageService service, Message<?> message) ;
    void onPostProcess(MessageService service, Message<?> message) ;

    void onErrorMessage(MessageService service, Message<?> message) ;
    void onRejectMessage(MessageService service, Message<?> message) ;
    void onRetryMessage(MessageService service, Message<?> message) ;
  }


```

The MessageService is designed to implement the logic to process the message, while the plugin is designed to reuse logic across the service, for example the plugin to log the message or monitor the message at each service point .

**Question**: Should we implement the service manager ourself to allow the service configuration and manage the dependenciesor we should reuse the framework such spring , osgi....

##Rest Client##

###Requirement###

1. The rest client should be able to take in a list of url connection or retrieve the list of url from zookeeper.
2. The client should be able to send a message or a batch message in the synchronous mode or asychronous mode.
  - Synchronous: The client should be able to send a message or a batch or messages in json format to the server. The server will reply an ackknowledg in json format, depend on the acknowledge status, the client should continue , retry or give up.
  - Asynchronous: The client should be able to send a stream  of messages or a stream of a batch messages to the server. For each set of messages or batch of messages, the server will send back an ackowledge.  Depend on the status of the acknowledge, the client should continue, retry or move the stream of message to another url handler or give up.
3. The client shoudl select the url in round robin mode(or other algorithm) and send the message to the http server. If the the client fail to send to an url , it should pick another url and retry. 
4. Plug in, the client should be able to plug in the interceptor such debug and trace interceptor so each time a message is sent, retried or failed, the interceptor will add the log to the message.
5. In case the client send a message to the server, the server able to handle the message but cannot send back an acknowledge due to the system or network overloaded. How should we handle? What should be the try strategy and the max number of retry 
 
###Current implementation and status###

1. Implement a client that can  take in a list of url, and different http client implementation
2. Implement http client with vertx http client and apache http client
3. The client is able to send the message to the http server in synchronous mode and use round robin algorithm. 
4. No retry, error handling and plugin yet.
5. Some code for unit test and integration test.

###TODO###

1. handle asynchronous mode
2. handle error and retry
3. Implement a more sophisticated algorithm with zookeeper to remove death url or add url automatically and dynamically.
4. Test, test... unit test, failed test, integration test, stress test!

What should be othe priority ?

##Sparkngin(HTTP Rest Service)##

###Requirement###

1. Implement an HTTP service that can handle the synchronous and asynchronous messages as describe in the client.
2. Forward the message to the queuengin(kafka/kinesis). The service should send back an OK or FAILD acknowledge to the client for each set of messages. 
3. Register the service and the url connection with a zookeeper server.
4. Plug in, the service should be able to plug in the interceptor such debug and trace interceptor so each time a message is sent, retried or failed, the interceptor will add the log to the message.

###Current implementation and status###

1. Implement the service that can handle synchronous messages with jetty and vertx
2. Some code to embed vertx engine and simulate the http cluster environment
3. Some code for unit test and integration test. 

###TODO###

1. handle asynchronous mode
2. register the service with zookeeper.
3. plugin

##Queuengin##

###Requirement###

**The queue structure:**

```
 topic/
       config
       error
       retry
       message
       test
       other?
```
- config: the config topic allow the client to send a configuration update across the system, for example, the client want to turn on the trace and debug mode for certain topic. Should we use the other way such register the topic configuration with zookeeper?
- error: the error topic allow a tier to put back a message that it(the tier) cannot process, for example the message contains some strange encoding or not in json format
- retry: The retry topic allow a a tier put back the message when it cannot process due to another service/tier is not available. For example, the scribengin dequeue a message, but it cannot write the message to Hbase due to the Hbase is down. In this case the scribengin can put back the message into the retry queue to process later. The problem is the message can be run out of the order. Kafka allows you to control the commit or use the auto commit mode. We can control the commit to commit the messages have been successfully processed. But I am not sure we can do the same thing with the other queuengin such kinesis.
- message: The message queue is the real queue for the topic where the consumer can enqueue and dequeue.
- test: use for the test purpose. For example, the demandspike can setup a test strategy with a number of error, failed, retried message for certain tier, at the end of the test, the client can send the expected statistic across the system so each tier can assert the statistic result. Maybe we can use with the config queue ? 

1. Implement api for the queue consumer and producer. The api is the wrapper for the kafka, kinesis queue engine 
2. Implement the real consumer and producer for kafka
3. Implement the real consumer and producer for kinesis
4. Plug in, the service should be able to plug in the interceptor such debug and trace interceptor so each time a message is sent, retried or failed, the interceptor will add the log to the message.

###Current implementation and status###

1. Implement the simple structure for the message
2. Implement the simple api for consumer and producer
3. Implement the simple consumer and producer for kafka
4. Some code to embed kafka engine and simulate the kafka cluster environment
5. Some code for unit test and integration test. 

###TODO###

1. Review the message structure
2. Discuss and implement the queue structure
3. Implement consumer and producer for kinesis
4. Plugin 

##Scribengin##

###Requirement###

1. Define the MessageWriter api
2. Implement an InmemoryDB or MockDB and the writer for the MockDB
3. Implement a ReportMessageWriter, the writer simply dequeue the message from the queuengin and collect the message statistic in the message log. This is convenient tool to test and monitor.
4. Implement the writer for HBase
5. Implement the writer for Elasticsearch
6. Plugin

###Current implementation and status###

1. Just some message writer api , mock db and some unit test.

###TODO###


##Demandspike##

###Requirement###

1. Define the MessageGenerator api, the message can be read from db , batch file , db or generated automatically.
2. Define test strategy configuration, like number of error,dropped,failed message by http service, scribengin.
3. Engine to send the configuration and the message. The engine should collect the instruction statistic for each tier/service. At the end, the engine should send the expected statistic across the system so each tier/service can assert the result.
4. Implement the plugin to test for sparkngin , queuengin, scribengin. 

###Current implementation and status###

1. Just some api and unit test.

###TODO###

##Release##

###Requirement###

1. To pull the denpencies project such zookeeper, kafka, vertx
2. Patch the dependencies with the custom script and plugin if necessary
3. Build the NeverwinterDP release with the structure
```
  NeverwinterDP/
     zookeeper(with patch)
     queuengin(kafka with patch script and queuengin plugin)
     sparkngin(vertx with patch script and sparkngin, queuengin plugin)
     scribengin
     demandspike
     bin(convenient script to launch all the services)
```


###Current implementation and status###

1. None

###TODO###


#Code Organization#

**Directory Structure**

```
  code
    -  lib
         +common
         +test-framework
    -  queuengin
    -  sparkngin
    -  scribengin
    -  demandspike
    -  release
```

1. The project lib/common project contains the common classess and util classes.
2. The project lib/test-framework contains the api and classes for the test, mock and embbeded environment.
3. The project queuengin contains the api and implementation for the kafka and kinesis queue.
4. The project sparkngin contains the code for Vertx Http Server and Jetty Http Server. It also contains the client code that allow to send message to the http server.
5. The project scribengin contain the message writer api and various writer implementation that allow to write the message to different data repository such hbase , hdfs, elasticsearch...
6. The demandspike contains a generic framework that allow to generate and send different type of message to the sparkngin
7. The project release contains the code and script to build and release sparkngin, queuengin, scribengin, demandspike as the independant distribution.

#Build And Develop#

##Gradle##
1. To build
  - cd NeverwinterDP/code
  - gradle clean build
2. Other

##Eclipse##
1. To generate the eclipse configuration
  - cd NeverwinterDP/code
  - gradle eclipse
2. Open eclipse
  - Choose File > Import
  - Choose General > Existing Projects into Workspace
  - Check Select root directory and browse to path/NeverwinterDP/code
  - Select all the projects except the code project, then click Finish
