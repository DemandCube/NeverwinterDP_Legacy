package com.neverwinterdp.queuengin;

import java.util.List;

public interface MessageProducer {
  public void send(String topic, Message<?> msg) throws Exception ;
  public void send(String topic, List<Message<?>> messages) throws Exception ;
  public void close() ;
}
