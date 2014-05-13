package com.neverwinterdp.scribengin;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.queuengin.MessageConsumerHandler;

public class ScribeMessageHandler implements MessageConsumerHandler {
  private MessageWriter writer ;
  
  public ScribeMessageHandler(MessageWriter writer) {
    this.writer = writer ;
  }
  
  public void onMessage(Message message) {
    writer.write(message) ;
  }

  public void onErrorMessage(Message message, Throwable error) {
  }
}