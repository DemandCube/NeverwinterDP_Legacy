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

##Cluster, Server And Service Management##

```
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * The server can be understood as a single container or a machine that contains an unlimited number
 * of the services.
 */
public class Server {
  /**
   * The configuration for the server such name, group, version, description, listen port. It also
   * may contain the service configurations
   * */
  private ServerConfig serverConfig ;
  /**
   * The server descriptor contain the information such hostname, listen port, the state of the server,
   * and the available services that the server provide with the service running status. 
   */
  private ServerDescriptor descriptor ;
  
  public ServerConfig getServerConfig() { return this.serverConfig ; }
  
  public void  setServerConfig(ServerConfig config) { this.serverConfig = config ; }
  
  public ServerDescriptor getServerDescriptor() { return descriptor ; }
  
  /**
   * This lifecycle method be called after the configuration is set. The method should:
   * 1. Compute the configuration and add the services with the service configuration.
   * 2. Loop through the services and call service.onInit()
   * 3. Set the state of the services to init.
   * 4. Set the state of the server to init. 
   * 5. Add and configure the cluster services, start the cluster services
   */
  public void onInit() {
    
  }
  
  /**
   * This method lifecycle should be called after the method onInit() is called. 
   * This method should be called by the external service that can access the Server instance 
   * or the cluster services.
   * 
   * This method should:
   * 1. Check the state of the server, if the state of the server is already START, then return
   * 2. Loop through all the services, call service.start().
   */
  public void startServices() {
    
  }
  
  /**
   * This method is used to stop all the services usually it is used to simmulate the 
   * server shutdown or suspend.
   */
  public void stopServices() {
  
  }
  
  /**
   * This method is called after the stopServices is called. This method should:
   * 1. Stop and destroy all the cluster services
   * 2. Release all the resources if necessary, save the monitor or profile information.
   */
  public void onDestroy() {
    
  }
  
  /**
   * This method is used to get a ClusterService. All the cluster service should be configured,
   * initialized and start in the onInit(). All the cluster service should live with the server
   * until the server instance is destroyed.
   * @param serviceId
   * @return
   */
  public ClusterService getClusterService(String serviceId) {
    return null ;
  }
  
  /**
   * This method is used to find a specifice service by the service id
   * @param serviceId
   * @return
   */
  public Service getService(String serviceId) {
    return null ;
  }
  /**
   * This method is used to find a specifice service by the service descriptor
   * @param descriptor
   * @return
   */
  public Service getService(ServiceDescriptor descriptor) {
    return getService(descriptor.getServiceId()) ;
  }
  
  public List<Service> getServices() {
    return null ;
  }
  
  /**
   * This method is used to dynamically add a service
   * @param service
   */
  public void register(Service service) {
    
  }
  
  /**
   * This method is used to dynamically remove a service
   * @param service
   */
  public void remove(String serviceId) {
    
  }
  
  

  /**
   * This method is designed to run certain command on a service such start, stop, ping to check 
   * the state of the service. 
   * 1. The method should find the the registered service by the service descriptor in the command
   * 2. Call the method command.execute(server, service) method.
   * 3. Handle the exception such service not found or the command execute throw an exception.
   * 4. This method cannot be called only if the server state is RUNNING.
   * @param command
   * @return
   */
  public <T> T execute(ServiceCommand<T> command) {
    return null ;
  }
  
  /**
   * This method is designed to be called by the cluster service only. When the server is in the 
   * SHUTDOWN state, the cluster service is still functioned and listen to the cluster command
   * @param command
   * @return
   */
  public <T> T execute(ServerCommand<T> command) {
    return null ;
  }
}

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * This is a service or a service wrapper to another project such zookeeper , kafka, Vertx...
 */
public interface Service {
  /**
   * The service config contains the configuration information such service id, service version,
   * description, The real class implementation for this service interface. 
   * @return
   */
  public ServiceConfig     getServiceConfig() ;
  
  public void setServiceConfig(ServiceConfig config); 

  /**
   * The service descriptor contain the information of the service such name , version , the state
   * of the service so another service or remote service can decide to use this service or not. 
   * @return
   */
  public ServiceDescriptor getServiceDescriptor() ;
  
  /**
   * this method is called when the server init
   * @param server
   */
  public void onInit(Server server) ;
  
  /**
   * this method is called when the server destroy, the service should release all the resources
   * in this method.
   * @param server
   */
  public void onDestroy(Server server) ;
  
  /**
   * This method is designed to start the service and change the service state to START. 
   * If the service is a wrapper to another service such zookeeper, kafka... All the real service
   * state such load, config, init, start should be implemented in this method 
   */
  public void start() ;
  
  /**
   * This method is designed to stop the service and change the service state to STOP. 
   * If the service is a wrapper to another service such zookeeper, kafka... All the real service
   * state such stop, destroy should be implemented in this method 
   */
  public void stop() ;
}

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

###Message API Proposal###
```
  public class Message {
    private MessageHeader            header = new MessageHeader();
    private MessageData              data;
    private List<MessageTrace>       traces;
    private List<MessageInstruction> instructions;
  }

  public class MessageHeader {
    private float      version;
    private String     topic;
    private String     key;
    private boolean    traceEnable;
    private boolean    instructionEnable;
  }
  
  public class MessageData {
    static public enum SerializeType { json, xml, binary }
  
    private String        type;
    private byte[]        data;
    private SerializeType serializeType;
  }
  
  public class MessageTrace {
    private String host      ;
    private String serviceId ;
    private float  serviceVersion ;
    private long   processTime ;
    private String message ;
  }
  
  public class MessageInstruction {
    private String targetService ;
    private String instruction ;
    private Map<String, String> params ;
  }
```

###MessageService API Proposal###

```
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 *                         +-----------------+   +-----------------+   +-----------------+
 *                         | MessageServer   |   | MessageServer   |   | MessageServer   |
 *                         +-----------------+   +-----------------+   +-----------------+
 * +--------+              |                 |   |                 |   |                 |
 * | Client |---Message--->| MessageServices |-->| MessageServices |-->| MessageServices |
 * +--------+              |  (Sparkngin)    |   |   (Queuengin)   |   |  (Scribengin)   |
 *                         +-----------------+   +-----------------+   +-----------------+
 * MessageService is composed of several components that can be configured, reused or replaced by
 * the different implementation:
 * 1. MessageServiceDescriptor is the service configuration with the properties such service name,
 *    service id, service version, description, the topics that the service are listened to.
 * 2. MessageProcessor: the logic to process the message such forward to sink, 
 *    route to another topic...
 * 3. MessageServicePlugin is designed to reuse certain code and logic such add trace log to the 
 *    message, monitor... The plugin is called before and after the message is processed with 2 
 *    methods onPreProcess(Message) and onPostProcess(Message)
 * 3. MessageErrorHandler is designed to handle the exception when the MessageProcessor throw the 
 *    exceptions such Rejected, Error, Retry, Unknown. 
 */
public class MessageService {
  private MessageServiceDescriptor descriptor ;
  private MessageProcessor processor ;
  private List<MessageServicePlugin> messagePlugins ;
  private List<MessageErrorHandler>  errorHandlers ;
  
  public MessageServiceDescriptor getDescriptor() { return descriptor ; }
  public void setDescriptor(MessageServiceDescriptor descriptor) {
    this.descriptor = descriptor ;
  }
  
  public void onInit() {
    for(MessageServicePlugin plugin : messagePlugins) {
      plugin.onInit() ;
    }
  }
  
  public void onDestroy() {
    for(MessageServicePlugin plugin : messagePlugins) {
      plugin.onDestroy() ;
    }
  }
  
  void process(Message message) {
    try { 
      for(MessageServicePlugin plugin : messagePlugins) {
        plugin.onPreProcess(this, message);
      }
      processor.process(this, message) ;
      for(MessageServicePlugin plugin : messagePlugins) {
        plugin.onPostProcess(this, message);
      }
    } catch(MessageException ex) {
      Type type = ex.getType() ;
      if(Type.REJECTED.equals(type)) {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onReject(this, message);
        }
      } else if(Type.ERROR.equals(type)) {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onError(this, message);
        }
      } else {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onUnknown(this, message);
        }
      }
    }
  }
}
```

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

##Release##

1. To build
  - cd NeverwinterDP/code
  - gradle clean build
2. To Release
  - Download kafka version _2.8.0-0.8.1.1, extract and save in the NeverwinterDP-dependencies/kafka_2.8.0-0.8.1.1, the NverwinterDP-dependencies shoule be at the same level with the repository NeverwinterDP directory  
  - cd NeverwinterDP/code/release
  - gradle clean release
  - You will find the Queuengin, Sparkngin, Scribengin in the build/release directory
3. To Test
  - Run Queuengin(Refer to kafka document)
    - Open a console, cd NeverwinterDP/code/release/Queuengin/bin, run ./zookeeper-server-start.sh ../config/zookeeper.properties
    - Open a console, cd NeverwinterDP/code/release/Queuengin/bin, run ./kafka-server-start.sh ../config/server.properties
    - Open a console, cd NeverwinterDP/code/release/Queuengin/bin, run ./queuengin.sh hello
    - Run ./queuengin.sh hello for more options 
  - Run Sparkngin
    - Keep zookeeper and kafka server running
    - Open a console, cd NeverwinterDP/code/release/Sparkngin/bin, run ./sparkngin.sh run to start the http service
    - Open a console, cd NeverwinterDP/code/release/Sparkngin/bin, run ./sparkngin.sh hello to send some messages to the queuengin. If the kafka server is not running, You should get some Error Ack.
  - Run Scribengin
    - Keep zookeeper and kafka server running
    - Open a console, cd NeverwinterDP/code/release/Scribengin/bin, run ./scribengin run --topic=HelloSparkngin, this will consume the messages that send by sparkngin.sh hello command.
