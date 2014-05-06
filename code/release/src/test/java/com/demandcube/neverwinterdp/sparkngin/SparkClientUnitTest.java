package com.demandcube.neverwinterdp.sparkngin;

import junit.framework.Assert;

import org.junit.Test;

import com.demandcube.neverwinterdp.ClusterUnitTest;
import com.demandcube.neverwinterdp.SampleEvent;
import com.neverwinterdp.queue.JSONMessage;
import com.neverwinterdp.queue.JSONMessageConsumerHandler;
import com.neverwinterdp.queue.JSONMessage.Log;
import com.neverwinterdp.queue.kafka.KafkaJSONMessageConsumerConnector;
import com.neverwinterdp.sparkngin.SparkAcknowledge;
import com.neverwinterdp.sparkngin.SparknginClient;

public class SparkClientUnitTest extends ClusterUnitTest {
  
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
      SparkAcknowledge ack = client.sendJSONMessage(topic, "m" + i, event, true) ;
    }
    
    JSONMessageConsumerHandlerImpl handler = new JSONMessageConsumerHandlerImpl() ;
    KafkaJSONMessageConsumerConnector<SampleEvent> consumer = 
        new KafkaJSONMessageConsumerConnector<SampleEvent>("consumer", zkCluster.getConnectURLs()) ;
    consumer.consume("test-topic", handler, 1) ;
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
        if(jsonMessage.getLogs() != null) {
          for(Log log : jsonMessage.getLogs()) {
            System.out.println("  " + log.toString()) ;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
