package com.neverwinterdp.queuengin.kinesis;

import java.util.List;

import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.queuengin.JSONMessageProducer;

public class KinesisJSONMessageProducer implements JSONMessageProducer {
  public void send(String topic, JSONMessage<?> msg) throws Exception {
  }
  
  public void send(String topic, List<JSONMessage<?>> messages) throws Exception {
  }
  
  public void close() { 
  }
  
}
