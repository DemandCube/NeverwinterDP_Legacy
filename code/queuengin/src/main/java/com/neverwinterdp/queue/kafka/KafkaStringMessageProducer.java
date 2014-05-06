package com.neverwinterdp.queue.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaStringMessageProducer {
  private Producer<String, String> producer;
  private String  topic;

  public KafkaStringMessageProducer(String topic, String kafkaBrokerUrls) {
    Properties props = new Properties() ;
    props.put("serializer.class",     "kafka.serializer.StringEncoder");
    props.put("metadata.broker.list", kafkaBrokerUrls);
    props.put("partitioner.class", SimplePartitioner.class.getName());
    props.put("request.required.acks", "1");
    this.producer = new Producer<String, String>(new ProducerConfig(props));
    this.topic = topic;
  }

  public void send(String message) {
    producer.send(new KeyedMessage<String, String>(topic, message));
    System.out.println("Sent Message: " + message) ;
  }
  
  public void send(String key, String message) {
    producer.send(new KeyedMessage<String, String>(topic, key, message));
    System.out.println("Sent Message: " + message) ;
  }
  
  public void send(List<String> message) {
    List<KeyedMessage<String, String>> holder = new ArrayList<KeyedMessage<String, String>>() ;
    for(int i = 0; i < message.size(); i++) {
      holder.add(new KeyedMessage<String, String>(topic, message.get(i))) ;
    }
    producer.send(holder);
  }
  
  public void close() { producer.close() ; }
  
}
