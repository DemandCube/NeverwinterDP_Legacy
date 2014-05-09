package com.neverwinterdp.queuengin.kafka;

import org.junit.Assert;
import org.junit.Test;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.testframework.event.SampleEvent;

public class MessageConsumerUnitTest extends ClusterUnitTest {
  @Test
  public void testMessageConsumer() throws Exception {
    String topic = "test-topic" ;
    int numOfMessages = 5 ;
    KafkaMessageProducer producer = new KafkaMessageProducer(kafkaCluster.getConnectionURLs()) ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      Message<SampleEvent> jsonMessage = new Message<SampleEvent>("m" + i, event, false) ;
      producer.send(topic,  jsonMessage) ;
    }
   
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    KafkaMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume(topic, handler, 1) ;
    Thread.sleep(2000) ;
    Assert.assertEquals(numOfMessages, handler.messageCount()) ;
  }
}