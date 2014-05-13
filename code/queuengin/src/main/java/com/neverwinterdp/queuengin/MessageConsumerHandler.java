package com.neverwinterdp.queuengin ;

import com.neverwinterdp.message.Message;

public interface MessageConsumerHandler {
  public void onMessage(Message message)  ;
  public void onErrorMessage(Message message, Throwable error)  ;
}