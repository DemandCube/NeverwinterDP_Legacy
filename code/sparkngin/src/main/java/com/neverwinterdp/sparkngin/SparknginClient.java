package com.neverwinterdp.sparkngin;

public class SparknginClient {
  private int currentIdx ;
  private SimpleSparknginHttpClient[] client ;
  
  public SparknginClient(String[] urls) {
    client = new SimpleSparknginHttpClient[urls.length] ;
    for(int i = 0; i < urls.length; i++) {
      client[i] = new SimpleSparknginHttpClient(urls[i]) ;
    }
  }
  
  public <T> SparkAcknowledge send(String topic, String key, T obj, boolean logEnable) throws Exception {
    //TODO: if the client fail to send, remove the client from the list
    //      retry another one
    SimpleSparknginHttpClient client = next() ;
    return client.send(topic, key, obj, logEnable) ;
  }
  
  synchronized SimpleSparknginHttpClient next() {
    if(currentIdx == client.length) currentIdx = 0;
    return client[currentIdx++] ;
  }
}
