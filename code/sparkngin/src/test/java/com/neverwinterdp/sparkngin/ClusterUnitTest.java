package com.neverwinterdp.sparkngin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.queuengin.kafka.KafkaCluster;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.sparkngin.SparkAcknowledge;
import com.neverwinterdp.sparkngin.SparknginClient;
import com.neverwinterdp.testframework.cluster.NeverwinterDPCluster;
import com.neverwinterdp.testframework.cluster.ServiceCluster;
import com.neverwinterdp.testframework.cluster.ZookeeperCluster;
import com.neverwinterdp.testframework.event.SampleEvent;

abstract public class ClusterUnitTest {
  protected NeverwinterDPCluster cluster ;
  protected ZookeeperCluster zkCluster ;
  protected KafkaCluster kafkaCluster ;
  
  @Before
  public void setup() throws Exception {
    cluster = new NeverwinterDPCluster("target/cluster") ;
    cluster.cleanup();
    
    zkCluster = new ZookeeperCluster(ZookeeperCluster.ZK_CLIENT_PORT, 1);
    cluster.add(zkCluster) ;
    
    kafkaCluster = 
        new KafkaCluster(KafkaCluster.KAFKA_PORT, 2).
        setZookeeperConnectURL(zkCluster.getConnectURLs(",")) ;
    cluster.add(kafkaCluster);

    cluster.add(createSparknginServiceCluster()) ;
    
    cluster.startup(); 
  }
  
  @After
  public void teardown() throws Exception {
    cluster.shutdown() ;
  }
  
  abstract protected ServiceCluster createSparknginServiceCluster() ;
  
  
  @Test
  public void testSend() throws Exception {
    int numOfMessages = 5 ;
    
    String[] connectionUrls = {
        "http://127.0.0.1:8080", "http://127.0.0.1:8081"
    };
    
    SparknginClient client = new SparknginClient(connectionUrls) ; ;
    String topic = "test-topic" ;
    for(int i = 0; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      try {
        SparkAcknowledge ack = client.send(topic, "m" + i, event, true) ;
        System.out.println("ack = " + ack);
      } catch(Throwable t) {
        t.printStackTrace(); 
      }
    }
    
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    KafkaMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume("test-topic", handler, 1) ;
    Thread.sleep(3000) ;
    Assert.assertEquals(numOfMessages, handler.messageCount()) ;
  }
}