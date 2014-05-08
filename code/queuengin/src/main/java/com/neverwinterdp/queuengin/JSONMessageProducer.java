package com.neverwinterdp.queue;

import java.util.List;

public interface JSONMessageProducer {
  public void send(String topic, JSONMessage<?> msg) throws Exception ;
  public void send(String topic, List<JSONMessage<?>> messages) throws Exception ;
  public void close() ;
}
