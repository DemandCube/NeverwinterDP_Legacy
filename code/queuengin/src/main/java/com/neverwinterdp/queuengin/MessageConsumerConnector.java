package com.neverwinterdp.queuengin ;


public interface MessageConsumerConnector<T> {
  public void consume(String topic, MessageConsumerHandler<T> handler, int numOfThreads) throws Exception ;
  public void close() ;
}