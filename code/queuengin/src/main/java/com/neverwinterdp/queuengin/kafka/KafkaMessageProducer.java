package com.neverwinterdp.queuengin.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.MessageProducer;
import com.neverwinterdp.util.JSONSerializer;

public class KafkaMessageProducer implements MessageProducer {
  private String name ;
  private Producer<String, String> producer;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public KafkaMessageProducer(String kafkaBrokerUrls) {
    Properties props = new Properties() ;
    props.put("serializer.class",     "kafka.serializer.StringEncoder");
    props.put("metadata.broker.list", kafkaBrokerUrls);
    props.put("partitioner.class", SimplePartitioner.class.getName());
    props.put("request.required.acks", "1");
    producer = new Producer<String, String>(new ProducerConfig(props));
  }

  public void send(String topic, Message msg) throws Exception {
    String data = JSONSerializer.INSTANCE.toString(msg) ;
    producer.send(new KeyedMessage<String, String>(topic, msg.getHeader().getKey(), data));
  }
  
  public void send(String topic, List<Message> messages) throws Exception {
    List<KeyedMessage<String, String>> holder = new ArrayList<KeyedMessage<String, String>>() ;
    for(int i = 0; i < messages.size(); i++) {
      Message m = messages.get(i) ;
      String data = JSONSerializer.INSTANCE.toString(m) ;
      holder.add(new KeyedMessage<String, String>(topic, m.getHeader().getKey(), data)) ;
    }
    producer.send(holder);
  }
  
  public void close() { producer.close() ; }
  
}
