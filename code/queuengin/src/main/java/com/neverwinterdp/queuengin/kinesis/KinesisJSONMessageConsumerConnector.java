package com.neverwinterdp.queuengin.kinesis ;

import java.io.IOException;

import com.neverwinterdp.queuengin.JSONMessageConsumerConnector;
import com.neverwinterdp.queuengin.JSONMessageConsumerHandler;

public class KinesisJSONMessageConsumerConnector<T> implements JSONMessageConsumerConnector<T>{
  public void consume(String topic, JSONMessageConsumerHandler<T> handler, int numOfThreads) throws IOException {
  }
}