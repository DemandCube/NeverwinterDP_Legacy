package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public interface SparknginSimpleHttpClient {
  public String getConnectionUrl() ;  
  public void send(String topic, Message message, SendMessageHandler handler) ;
}