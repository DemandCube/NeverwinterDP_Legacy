package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public class SparknginHttpClient {
  private int currentIdx = 0;
  private SparknginSimpleHttpClient[] client ;
  
  public SparknginHttpClient(SparknginSimpleHttpClient[] client) {
    this.client = client; 
  }
  
  public void send(String topic, Message message, SendMessageHandler handler) {
    //TODO: if the client fail to send, remove the client from the list
    //      retry another one
    SparknginSimpleHttpClient client = next() ;
    client.send(topic, message, handler) ;
  }
  
  synchronized SparknginSimpleHttpClient next() {
    if(currentIdx == client.length) currentIdx = 0;
    return client[currentIdx++] ;
  }
}
