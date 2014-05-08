package com.neverwinterdp.queue.kafka;

import junit.framework.Assert;

import org.junit.Test;

import com.neverwinterdp.queue.StringMessageConsumerHandler;
import com.neverwinterdp.queue.kafka.KafkaMessageConsumer;
import com.neverwinterdp.queue.kafka.KafkaStringMessageConsumerConnector;
import com.neverwinterdp.queue.kafka.KafkaStringMessageProducer;

public class MessageConsumerUnitTest extends ClusterUnitTest {
  @Test
  public void testConsumer() throws Exception {
    testMessageConsumer() ;
    testMessageConsumerConnector() ;
  }
  
  public void testMessageConsumer() throws Exception {
    String topic = "MessageConsumer" ;
    KafkaStringMessageProducer producer = new KafkaStringMessageProducer(topic, kafkaCluster.getConnectionURLs()) ;
    producer.send("first",  "first message") ;
    producer.send("second", "second message") ;
    Thread.sleep(1000) ;
    
    KafkaMessageConsumer consumer = new KafkaMessageConsumer("SimpleMessageConsumer1", "127.0.0.1", 9090) ;
    MessageConsumerHandlerImpl handler = new MessageConsumerHandlerImpl() ;
    consumer.consume(topic, handler) ;
    Assert.assertEquals(2, handler.count) ;
    
    handler = new MessageConsumerHandlerImpl() ;
    consumer = new KafkaMessageConsumer("SimpleMessageConsumer2", "127.0.0.1", 9090) ;
    consumer.consume(topic, handler) ;
    Assert.assertEquals(2, handler.count) ;
  }
  
  public void testMessageConsumerConnector() throws Exception {
    String topic = "MessageConsumerConnector" ;
    int numOfMessages = 10 ;
    KafkaStringMessageProducer producer = new KafkaStringMessageProducer(topic, kafkaCluster.getConnectionURLs()) ;
    for(int i = 0 ; i < numOfMessages; i++) {
      producer.send("key " + i, "message " + i) ;
    }
    
    KafkaStringMessageConsumerConnector connector = new KafkaStringMessageConsumerConnector("consumer", zkCluster.getConnectURLs()) ;
    MessageConsumerHandlerImpl handler = new MessageConsumerHandlerImpl() ;
    connector.consume(topic, handler, 1) ;
    Thread.sleep(1000) ;
    Assert.assertEquals(numOfMessages, handler.count) ;
  }
  
  static public class MessageConsumerHandlerImpl implements StringMessageConsumerHandler {
    int count = 0 ;
    
    public void onMessage(String key, String message) {
      count++ ;
      System.out.println("Consume key = " + key + ", message = " + message);
    }
  }
}