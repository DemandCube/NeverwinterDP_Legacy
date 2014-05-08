package com.neverwinterdp.scribengin.dummy;


import org.junit.Assert;
import org.junit.Test;

import com.neverwinterdp.queuengin.Message;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.queuengin.kafka.KafkaMessageProducer;
import com.neverwinterdp.scribengin.MessageWriterHandler;
import com.neverwinterdp.testframework.event.SampleEvent;

public class MessageWriterUnitTest extends ClusterUnitTest {
  @Test
  public void testMessageWriter() throws Exception {
    String topic = "dummy" ;
    int numOfMessages = 10 ;
    KafkaMessageProducer producer = new KafkaMessageProducer(kafkaCluster.getConnectionURLs()) ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      Message<SampleEvent> jsonMessage = new Message<SampleEvent>("m" + i, event, false) ;
      producer.send(topic,  jsonMessage) ;
      System.out.println("send " + event.getName()) ;
    }
    
    InMemoryMessageDB db = new InMemoryMessageDB() ;
    MessageWriterHandler<SampleEvent> handler = new  MessageWriterHandler<SampleEvent>(db) ; 
    
    KafkaMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume(topic, handler, 1) ;
    
    Thread.sleep(2000) ;
    Assert.assertEquals(numOfMessages, db.count());
  }
}