package com.neverwinterdp.sparkngin;

import com.neverwinterdp.message.Message;

public class SparknginClient {
  private int currentIdx = 0;
  private SparknginSimpleClient[] client ;
  
  public SparknginClient(SparknginSimpleClient[] client) {
    this.client = client; 
  }
  
  public void send(String topic, Message message, SendMessageHandler handler) {
    //TODO: if the client fail to send, remove the client from the list
    //      retry another one
    SparknginSimpleClient client = next() ;
    client.send(topic, message, handler) ;
  }
  
  synchronized SparknginSimpleClient next() {
    if(currentIdx == client.length) currentIdx = 0;
    return client[currentIdx++] ;
  }
}
