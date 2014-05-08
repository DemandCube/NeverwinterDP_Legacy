package com.neverwinterdp.scribengin;

import com.neverwinterdp.queuengin.Message;
import com.neverwinterdp.queuengin.MessageConsumerHandler;

public class MessageWriterHandler<T> implements MessageConsumerHandler<T> {
  private MessageWriter writer ;
  
  public MessageWriterHandler(MessageWriter writer) {
    this.writer = writer ;
  }
  
  public void onMessage(Message<T> message) {
    writer.write(message) ;
  }
}