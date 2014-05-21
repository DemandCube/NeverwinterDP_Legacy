package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public interface SendMessageHandler {
  public void onResponse(Message message, SparknginSimpleClient client, SendAck ack) ;
  public void onError(Message message, SparknginSimpleClient client, Throwable error) ;
  public void onRetry(Message message, SparknginSimpleClient client) ;
}
