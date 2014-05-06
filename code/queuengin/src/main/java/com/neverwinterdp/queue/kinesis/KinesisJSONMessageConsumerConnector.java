package com.neverwinterdp.queue.kinesis ;

import java.io.IOException;

import com.neverwinterdp.queue.JSONMessageConsumerConnector;
import com.neverwinterdp.queue.JSONMessageConsumerHandler;

public class KinesisJSONMessageConsumerConnector<T> implements JSONMessageConsumerConnector<T>{
  public void consume(String topic, JSONMessageConsumerHandler<T> handler, int numOfThreads) throws IOException {
  }
}