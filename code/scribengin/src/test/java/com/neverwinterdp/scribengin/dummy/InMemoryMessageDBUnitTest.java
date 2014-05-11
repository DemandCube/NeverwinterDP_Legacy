package com.neverwinterdp.scribengin.dummy;


import org.junit.Assert;
import org.junit.Test;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.MessageGenerator;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.scribengin.ClusterUnitTest;
import com.neverwinterdp.scribengin.ScribeMessageHandler;
import com.neverwinterdp.testframework.event.SampleEvent;

public class InMemoryMessageDBUnitTest extends ClusterUnitTest {
  @Test
  public void testMessageWriter() throws Exception {
    String topic = "InMemoryDB" ;
    int numOfMessages = 10 ;
    
    MessageGenerator<SampleEvent> generator = new MessageGenerator<SampleEvent>(numOfMessages) {
      protected Message<SampleEvent> createMessage(long seq) {
        SampleEvent event = new SampleEvent("event-" + seq, "event " + seq) ;
        Message<SampleEvent> m = new Message<SampleEvent>("m" + seq, event, true) ;
        return m ;
      }
    };
    generator.send(kafkaCluster.createProducer(), topic);
    
    InMemoryMessageDB db = new InMemoryMessageDB() ;
    ScribeMessageHandler<SampleEvent> handler = new  ScribeMessageHandler<SampleEvent>(db) ; 
    
    KafkaMessageConsumerConnector<SampleEvent> connector = kafkaCluster.createConnector("consumer") ;
    connector.consume(topic, handler, 1) ;
    
    Thread.sleep(2000) ;
    Assert.assertEquals(numOfMessages, db.count());
  }
}