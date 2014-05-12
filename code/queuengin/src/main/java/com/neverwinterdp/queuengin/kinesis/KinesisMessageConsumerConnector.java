package com.neverwinterdp.queuengin.kinesis ;

import java.io.IOException;

import com.neverwinterdp.queuengin.MessageConsumerConnector;
import com.neverwinterdp.queuengin.MessageConsumerHandler;

public class KinesisMessageConsumerConnector<T> implements MessageConsumerConnector<T>{
  public void consume(String topic, MessageConsumerHandler<T> handler, int numOfThreads) throws IOException {
  }

  public void close() {
  }
}