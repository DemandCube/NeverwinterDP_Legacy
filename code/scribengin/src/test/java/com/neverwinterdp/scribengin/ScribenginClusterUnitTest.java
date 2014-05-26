package com.neverwinterdp.scribengin;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.message.SampleEvent;
import com.neverwinterdp.queuengin.kafka.KafkaMessageProducer;
import com.neverwinterdp.queuengin.kafka.cluster.KafkaClusterService;
import com.neverwinterdp.queuengin.kafka.cluster.ZookeeperClusterService;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerBuilder;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.HazelcastClusterClient;
import com.neverwinterdp.util.FileUtil;

public class ScribenginClusterUnitTest {
  static {
    System.setProperty("app.dir", "build/cluster") ;
    System.setProperty("app.config.dir", "src/app/config") ;
    System.setProperty("log4j.configuration", "file:src/app/config/log4j.properties") ;
  }
  
  static String TOPIC_NAME = "scribengin" ;
  
  static protected Server      zkServer, kafkaServer, scribenginServer ;
  static protected ClusterClient client ;

  @BeforeClass
  static public void setup() throws Exception {
    FileUtil.removeIfExist("build/cluster", false);
    ServerBuilder zkBuilder = new ServerBuilder() ;
    zkBuilder.addService(ZookeeperClusterService.class) ;
    zkServer = zkBuilder.build();
    
    
    ServerBuilder kafkaBuilder = new ServerBuilder() ;
    kafkaBuilder.addService(KafkaClusterService.class) ;
    kafkaServer = kafkaBuilder.build();
    
    ServerBuilder scribenginBuilder = new ServerBuilder() ;
    scribenginBuilder.
      addService(ScribenginClusterService.class).
      setParameter("zookeeperUrls", "127.0.0.1:2181").
      setParameter("topic", TOPIC_NAME) ;
    scribenginServer = scribenginBuilder.build();
    
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
    int numOfMessages = 50 ;
    KafkaMessageProducer producer = new KafkaMessageProducer("127.0.0.1:9092") ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      Message jsonMessage = new Message("m" + i, event, false) ;
      producer.send(TOPIC_NAME,  jsonMessage) ;
    }
    Thread.sleep(2000) ;
  }
}