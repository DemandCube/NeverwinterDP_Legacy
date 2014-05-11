package com.neverwinterdp.scribengin;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.MessageConsumerHandler;

public class ScribeMessageHandler<T> implements MessageConsumerHandler<T> {
  private MessageWriter writer ;
  
  public ScribeMessageHandler(MessageWriter writer) {
    this.writer = writer ;
  }
  
  public void onMessage(Message<T> message) {
    writer.write(message) ;
  }

  public void onErrorMessage(Message<T> message, Throwable error) {
  }
}