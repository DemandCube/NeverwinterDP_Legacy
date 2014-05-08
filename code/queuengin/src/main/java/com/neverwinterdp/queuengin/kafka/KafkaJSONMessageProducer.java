package com.neverwinterdp.queuengin.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.queuengin.JSONMessageProducer;
import com.neverwinterdp.util.JSONSerializer;

public class KafkaJSONMessageProducer implements JSONMessageProducer {
  private Producer<String, String> producer;

  public KafkaJSONMessageProducer(String kafkaBrokerUrls) {
    Properties props = new Properties() ;
    props.put("serializer.class",     "kafka.serializer.StringEncoder");
    props.put("metadata.broker.list", kafkaBrokerUrls);
    props.put("partitioner.class", SimplePartitioner.class.getName());
    props.put("request.required.acks", "1");
    producer = new Producer<String, String>(new ProducerConfig(props));
  }

  public void send(String topic, JSONMessage<?> msg) throws Exception {
    String data = JSONSerializer.INSTANCE.toString(msg) ;
    producer.send(new KeyedMessage<String, String>(topic, msg.getKey(), data));
  }
  
  public void send(String topic, List<JSONMessage<?>> messages) throws Exception {
    List<KeyedMessage<String, String>> holder = new ArrayList<KeyedMessage<String, String>>() ;
    for(int i = 0; i < messages.size(); i++) {
      JSONMessage<?> m = messages.get(i) ;
      String data = JSONSerializer.INSTANCE.toString(m) ;
      holder.add(new KeyedMessage<String, String>(topic, m.getKey(), data)) ;
    }
    producer.send(holder);
  }
  
  public void close() { producer.close() ; }
  
}
