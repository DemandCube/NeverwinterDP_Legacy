package com.neverwinterdp.sparkngin.jetty;


public class SparknginClient {
  private int currentIdx ;
  private SimpleSparknginClient[] client ;
  
  public SparknginClient(String[] urls) {
    client = new SimpleSparknginClient[urls.length] ;
    for(int i = 0; i < urls.length; i++) {
      client[i] = new SimpleSparknginClient(urls[i]) ;
    }
  }
  
  public <T> SparkAcknowledge sendJSONMessage(String topic, String key, T obj, boolean logEnable) throws Exception {
    //TODO: if the client fail to send, remove the client from the list
    //      retry another one
    SimpleSparknginClient client = next() ;
    return client.sendJSONMessage(topic, key, obj, logEnable) ;
  }
  
  synchronized SimpleSparknginClient next() {
    if(currentIdx == client.length) currentIdx = 0;
    return client[currentIdx++] ;
  }
}
