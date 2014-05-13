package com.neverwinterdp.scribengin;


import org.junit.Test;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.MessageGenerator;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.testframework.event.SampleEvent;

public class ReportMessageWriterUnitTest extends ClusterUnitTest {
  @Test
  public void testMessageWriter() throws Exception {
    String topic = "topic" ;
    int numOfMessages = 10000000 ;
    
    MessageGenerator generator = new MessageGenerator(numOfMessages) {
      protected Message createMessage(long seq) {
        SampleEvent event = new SampleEvent("event-" + seq, "event " + seq) ;
        Message m = new Message("m" + seq, event, true) ;
        return m ;
      }
    };
    Thread sender = generator.createSendWorker(kafkaCluster.createProducer(), topic);
    ReportMessageWriter writer = new ReportMessageWriter() ;
    ScribeMessageHandler handler = new  ScribeMessageHandler(writer) ; 
    
    KafkaMessageConsumerConnector connector = kafkaCluster.createConnector("consumer") ;
    connector.consume(topic, handler, 1) ;
    
    sender.start();
    Thread.sleep(10000) ;
    sender.interrupt(); 
    writer.getStatistics().report(System.out);
  }
}