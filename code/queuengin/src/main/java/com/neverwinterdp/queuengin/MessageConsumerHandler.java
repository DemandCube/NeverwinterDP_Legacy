package com.neverwinterdp.queuengin ;

public interface MessageConsumerHandler<T> {
  public void onMessage(Message<T> message)  ;
  public void onErrorMessage(Message<T> message, Throwable error)  ;
}