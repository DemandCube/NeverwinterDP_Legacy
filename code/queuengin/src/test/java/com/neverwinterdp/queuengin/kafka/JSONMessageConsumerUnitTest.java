package com.neverwinterdp.queuengin.kafka;

import org.junit.Assert;
import org.junit.Test;

import com.neverwinterdp.queuengin.DummyJSONMessageConsumerHandler;
import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.testframework.event.SampleEvent;

public class JSONMessageConsumerUnitTest extends ClusterUnitTest {
  @Test
  public void testJSONMessageConsumer() throws Exception {
    String topic = "JSONMessageConsumerConnector" ;
    int numOfMessages = 3 ;
    KafkaJSONMessageProducer producer = new KafkaJSONMessageProducer(kafkaCluster.getConnectionURLs()) ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      JSONMessage<SampleEvent> jsonMessage = new JSONMessage<SampleEvent>("m" + i, event, false) ;
      producer.send(topic,  jsonMessage) ;
    }
   
    DummyJSONMessageConsumerHandler handler = new DummyJSONMessageConsumerHandler() ;
    KafkaJSONMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaJSONMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume(topic, handler, 1) ;
    Thread.sleep(1000) ;
    Assert.assertEquals(numOfMessages, handler.messageCount()) ;
  }
}