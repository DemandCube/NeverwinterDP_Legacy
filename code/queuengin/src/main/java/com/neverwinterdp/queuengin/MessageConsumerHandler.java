package com.neverwinterdp.queuengin ;

public interface MessageConsumerHandler<T> {
  public void onMessage(Message<T> jsonMessage)  ;
}