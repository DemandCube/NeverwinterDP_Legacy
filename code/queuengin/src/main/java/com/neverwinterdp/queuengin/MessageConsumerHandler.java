package com.neverwinterdp.queuengin ;

import com.neverwinterdp.message.Message;

public interface MessageConsumerHandler<T> {
  public void onMessage(Message<T> message)  ;
  public void onErrorMessage(Message<T> message, Throwable error)  ;
}