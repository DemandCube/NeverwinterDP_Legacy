package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public interface SparknginSimpleHttpClient {
  public String getConnectionUrl() ;  
  public <T> void send(String topic, Message<T> message, SendMessageHandler handler) ;
}