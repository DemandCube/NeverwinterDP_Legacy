package com.neverwinterdp.queuengin ;


public interface MessageConsumerConnector {
  public void consume(String topic, MessageConsumerHandler handler, int numOfThreads) throws Exception ;
  public void close() ;
}