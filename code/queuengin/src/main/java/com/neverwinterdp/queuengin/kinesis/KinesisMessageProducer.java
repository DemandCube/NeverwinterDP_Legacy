package com.neverwinterdp.queuengin.kinesis;

import java.util.List;

import com.neverwinterdp.queuengin.Message;
import com.neverwinterdp.queuengin.MessageProducer;

public class KinesisMessageProducer implements MessageProducer {
  public void send(String topic, Message<?> msg) throws Exception {
  }
  
  public void send(String topic, List<Message<?>> messages) throws Exception {
  }
  
  public void close() { 
  }
  
}
