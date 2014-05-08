package com.neverwinterdp.scribengin;

import com.neverwinterdp.queuengin.Message;
import com.neverwinterdp.queuengin.MessageConsumerHandler;

public class MessageConsumerHandlerWriter<T> implements MessageConsumerHandler<T> {
  private MessageWriter writer ;
  
  public MessageConsumerHandlerWriter(MessageWriter writer) {
    this.writer = writer ;
  }
  
  public void onMessage(Message<T> message) {
    writer.write(message) ;
  }
}