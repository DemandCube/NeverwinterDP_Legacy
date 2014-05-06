package com.neverwinterdp.queue.kinesis;

import java.util.List;

import com.neverwinterdp.queue.JSONMessage;
import com.neverwinterdp.queue.JSONMessageProducer;

public class KinesisJSONMessageProducer implements JSONMessageProducer {
  public void send(String topic, JSONMessage<?> msg) throws Exception {
  }
  
  public void send(String topic, List<JSONMessage<?>> messages) throws Exception {
  }
  
  public void close() { 
  }
  
}
