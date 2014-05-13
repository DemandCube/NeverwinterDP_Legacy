package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public interface SendMessageHandler {
  public void onResponse(Message message, SparknginSimpleHttpClient client, SendAck ack) ;
  public void onError(Message message, SparknginSimpleHttpClient client, Throwable error) ;
  public void onRetry(Message message, SparknginSimpleHttpClient client) ;
}
