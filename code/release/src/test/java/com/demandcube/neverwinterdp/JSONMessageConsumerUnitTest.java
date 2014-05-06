package com.demandcube.neverwinterdp;

import junit.framework.Assert;

import org.junit.Test;

import com.neverwinterdp.queue.JSONMessage;
import com.neverwinterdp.queue.JSONMessageConsumerHandler;
import com.neverwinterdp.queue.kafka.KafkaJSONMessageConsumerConnector;
import com.neverwinterdp.queue.kafka.KafkaJSONMessageProducer;

public class JSONMessageConsumerUnitTest extends ClusterUnitTest {
  @Test
  public void testJSONMessageConsumer() throws Exception {
    String topic = "JSONMessageConsumerConnector" ;
    int numOfMessages = 3 ;
    KafkaJSONMessageProducer producer = new KafkaJSONMessageProducer(cluster.getKafkaCluster().getConnectionURLs()) ;
    for(int i = 0 ; i < numOfMessages; i++) {
      SampleEvent event = new SampleEvent("event-" + i, "event " + i) ;
      JSONMessage<SampleEvent> jsonMessage = new JSONMessage<SampleEvent>("m" + i, event, false) ;
      producer.send(topic,  jsonMessage) ;
    }
   
    JSONMessageConsumerHandlerImpl handler = new JSONMessageConsumerHandlerImpl() ;
    KafkaJSONMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaJSONMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume(topic, handler, 1) ;
    Thread.sleep(1000) ;
    Assert.assertEquals(numOfMessages, handler.count) ;
  }
  
  static public class JSONMessageConsumerHandlerImpl implements JSONMessageConsumerHandler<SampleEvent> {
    int count =  0 ;
    public void onJSONMessage(JSONMessage<SampleEvent> jsonMessage) {
      count++ ;
      try {
        SampleEvent event = jsonMessage.getDataAs(SampleEvent.class);
        System.out.println("Consume: " + event.getDescription());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}