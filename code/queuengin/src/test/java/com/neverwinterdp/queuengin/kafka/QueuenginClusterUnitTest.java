package com.neverwinterdp.queuengin.kafka;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.HazelcastClusterClient;
import com.neverwinterdp.server.config.Configuration;
import com.neverwinterdp.testframework.event.SampleEvent;
import com.neverwinterdp.util.FileUtil;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.JSONSerializer;

public class QueuenginClusterUnitTest {
  static {
    System.setProperty("app.dir", "build") ;
    System.setProperty("app.config.dir", "src/app/config") ;
    System.setProperty("log4j.configuration", "file:src/app/config/kafka/simple-log4j.properties") ;
  }
  
  static protected Server      zkServer, kafkaServer ;
  static protected ClusterClient client ;

  static Server createServer(String configFile) throws Exception {
    String jsonConfig = IOUtil.getFileContentAsString(configFile, "UTF-8") ;
    Configuration conf = JSONSerializer.INSTANCE.fromString(jsonConfig, Configuration.class) ;

    Server server = new Server() ;  
    server.setConfig(conf.getServer());
    server.onInit();
    server.getServiceContainer().register(conf.getServices());
    server.start();
    return server ;
  }
  
  @BeforeClass
  static public void setup() throws Exception {
    FileUtil.removeIfExist("build/data", false);
    zkServer =    createServer("src/app/config/zookeeper/server-config.json") ;
    kafkaServer = createServer("src/app/config/kafka/server-config.json") ;
    
    ClusterMember member = zkServer.getCluster().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new HazelcastClusterClient(connectUrl) ;
  }

  @AfterClass
  static public void teardown() throws Exception {
    client.shutdown(); 
    zkServer.exit(0) ;
  }
  
  @Test
  public void testSendMessage() throws Exception {
    Thread.sleep(5000);
    String topic = "test-topic" ;
    int numOfMessages = 5 ;
    KafkaMessageProducer producer = new KafkaMessageProducer("127.0.0.1:9092") ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      Message jsonMessage = new Message("m" + i, event, false) ;
      producer.send(topic,  jsonMessage) ;
    }
   
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    KafkaMessageConsumerConnector consumer = 
      new KafkaMessageConsumerConnector("consumer", "127.0.0.1:2181") ;
    consumer.consume(topic, handler, 1) ;
    Thread.sleep(2000) ;
    Assert.assertEquals(numOfMessages, handler.messageCount()) ;
  }
}