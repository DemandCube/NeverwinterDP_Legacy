package com.neverwinterdp.ringbearer.job.send;

import java.util.List;
import java.util.Map;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.kafka.KafkaMessageProducer;
import com.neverwinterdp.util.text.StringUtil;
import com.neverwinterdp.yara.MetricRegistry;

public class KafkaMessageDriver implements MessageDriver {
  private MetricRegistry mRegistry ;
  private String topic ;
  private KafkaMessageProducer producer ;
  
  public KafkaMessageDriver(MetricRegistry mRegistry) {
    this.mRegistry = mRegistry ;
  }
  
  public void init(Map<String, String> props, List<String> connect, String topic) {
    this.topic = topic ;
    String connectUrls = StringUtil.join(connect, ",") ;
    producer = new KafkaMessageProducer(props, mRegistry, connectUrls) ;
  }
  
  public void send(Message message) throws Exception {
    message.getHeader().setTopic(topic);
    producer.send(topic,  message) ;
  }
  
  public void close() { 
    producer.close();
  }
}