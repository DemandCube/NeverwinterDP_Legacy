package com.neverwinterdp.queue ;


public interface JSONMessageConsumerConnector<T> {
  public void consume(String topic, JSONMessageConsumerHandler<T> handler, int numOfThreads) throws Exception ;
}