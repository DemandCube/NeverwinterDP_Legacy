package com.neverwinterdp.queuengin;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.message.SampleEvent;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.queuengin.kafka.KafkaMessageProducer;

public class HelloQueuengin {
  static int MODE_QUIET = 0 ;
  static int MODE_VERBOSE = 1 ;
  
  static public class HelloProducer implements Runnable {
    String brokerList ;
    int numOfMessages = 10 ;
    String topic = "HelloQueuengin" ;
    int mode = 1 ;
    HelloProducer(String brokerList, String topic, int numOfMessages, int mode) {
      this.brokerList = brokerList ;
      this.topic = topic ;
      this.numOfMessages = numOfMessages ;
      this.mode = mode ;
    }
    
    public void run() {
      try {
        KafkaMessageProducer producer = new KafkaMessageProducer(brokerList) ;
        for(int i = 0 ; i < numOfMessages; i++) {
          SampleEvent event = new SampleEvent("event-" + i, "Hello Queuengin " + i) ;
          Message jsonMessage = new Message("m" + i, event, false) ;
          producer.send(topic,  jsonMessage) ;
          if(mode == MODE_VERBOSE) {
            System.out.println("Produce: " + event.getDescription()); 
          } else {
            if(i > 0 && i % 500 == 0) {
              System.out.println("Produce " + i + " messages");
            }
          }
        }
        if(mode != MODE_VERBOSE) {
          System.out.println("Produce " + numOfMessages + " messages");
        }
        producer.close();
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
  static public class HelloMessageConsumerHandler implements MessageConsumerHandler {
    private int count =  0 ;
    int mode = 1 ;
    
    HelloMessageConsumerHandler(int mode) {
      this.mode = mode ;
    }
    
    public int messageCount() { return count ; }
    
    public void onMessage(Message jsonMessage) {
      count++ ;
      try {
        SampleEvent event = jsonMessage.getData().getDataAs(SampleEvent.class);
        if(mode == MODE_VERBOSE) {
          System.out.println("Consume: " + event.getDescription()); 
        } else {
          if(count % 500 == 0) {
            System.out.println("Consume " + count + " messages");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void onErrorMessage(Message message, Throwable error) {
    }
    
    public void close() {
      if(mode != MODE_VERBOSE) {
        System.out.println("Consume " + count + " messages");
      }
    }
  }
  
  static String getValue(String arg) {
    String[] array = arg.split("=", 2) ;
    return array[1] ;
  }
  
  static public void main(String[] args) throws Exception {
    String topic = "HelloQueuengin" ;
    int numOfMessages = 10000 ;
    int mode = MODE_QUIET ;
    String brokerList = "127.0.0.1:9092" ;
    String zookeeperList = "127.0.0.1:2181" ;

    System.out.println("Available options: ");
    System.out.println("  --topic=hello");
    System.out.println("  --num-of-messages=10");
    System.out.println("  --booker-list=127.0.0.1:9092");
    System.out.println("  --zookeeper-list=127.0.0.1:2181");
    System.out.println("  --quiet or --verbose");
    if(args != null) {
      for(String arg : args) {
        if(arg.startsWith("--topic")) topic = getValue(arg) ;
        else if(arg.startsWith("--num-of-messages")) numOfMessages = Integer.parseInt(getValue(arg)) ;
        else if(arg.startsWith("--broker-list")) brokerList = getValue(arg) ;
        else if(arg.startsWith("--zookeeper-list")) zookeeperList = getValue(arg) ;
        else if(arg.startsWith("--quiet")) mode = MODE_QUIET ;
        else if(arg.startsWith("--verbose")) mode = MODE_VERBOSE ;
        else {
          System.out.println("Unknown option: " + arg);
          return ;
        }
      }
    }
    
    HelloProducer helloProducer = 
        new HelloProducer(brokerList, topic, numOfMessages, mode) ;
    Thread producerThread = new Thread(helloProducer) ;
    producerThread.start(); 
   
    HelloMessageConsumerHandler  handler = new HelloMessageConsumerHandler(mode) ;
    KafkaMessageConsumerConnector consumer = 
        new KafkaMessageConsumerConnector("consumer", zookeeperList) ;
    consumer.consume(topic, handler, 1) ;
    while(producerThread.isAlive()) {
      Thread.sleep(1000);
    }
    handler.close(); 
    consumer.close();
    System.exit(0);
  }
}
