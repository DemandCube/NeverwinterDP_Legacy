package com.neverwinterdp.sparkngin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.queuengin.Message;
import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.queuengin.kafka.KafkaCluster;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.sparkngin.SendAck;
import com.neverwinterdp.sparkngin.SparknginHttpClient;
import com.neverwinterdp.sparkngin.vertx.SparknginSimpleVertxHttpClient;
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
    
    String[] connectionUrls = { "http://127.0.0.1:8080", "http://127.0.0.1:8081" };
    SendMessageHandler sendHandler = new SendMessageHandler() {
      public void onResponse(Message<?> message, SparknginSimpleHttpClient client, SendAck ack) {
        System.out.println("Ack: " + ack.getStatus()) ;
      }

      public void onError(Message<?> message, SparknginSimpleHttpClient client, Throwable error) {
      }

      public void onRetry(Message<?> message, SparknginSimpleHttpClient client) {
      }
    };
    
    SparknginSimpleHttpClient[] simpleClients = SparknginSimpleVertxHttpClient.create(connectionUrls) ;
    SparknginHttpClient client = new SparknginHttpClient(simpleClients) ; ;
    String topic = "test-topic" ;
    for(int i = 0; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      Message<SampleEvent> message = new Message<SampleEvent>("m" + i, event, true) ;
      client.send(topic, message, sendHandler) ;
    }
    
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    KafkaMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume("test-topic", handler, 1) ;
    Thread.sleep(3000) ;
    Assert.assertEquals(numOfMessages, handler.messageCount()) ;
  }
}