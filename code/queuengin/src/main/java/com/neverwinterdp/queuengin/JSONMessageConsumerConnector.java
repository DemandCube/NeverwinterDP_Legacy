package com.neverwinterdp.queuengin ;


public interface JSONMessageConsumerConnector<T> {
  public void consume(String topic, JSONMessageConsumerHandler<T> handler, int numOfThreads) throws Exception ;
}