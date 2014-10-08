package com.neverwinterdp.ringbearer.job.send;

import java.util.List;
import java.util.Map;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.sparkngin.http.JSONHttpSparknginClient;
import com.neverwinterdp.yara.MetricRegistry;
import com.neverwinterdp.yara.Timer;

public class HttpSparknginMessageDriver implements MessageDriver {
  private MetricRegistry mRegistry ;
  private String topic ;
  private JSONHttpSparknginClient client ;
  
  public HttpSparknginMessageDriver(MetricRegistry mRegistry) {
    this.mRegistry = mRegistry ;
  }
  
  public void init(Map<String, String> props, List<String> connect, String topic) {
    this.topic = topic ;
    try {
      for(String selConnect : connect) {
        int separatorIdx = selConnect.lastIndexOf(":") ;
        String host = selConnect.substring(0, separatorIdx) ;
        int port = Integer.parseInt(selConnect.substring(separatorIdx + 1));
        client = new JSONHttpSparknginClient (host, port, 300, true) ;
      }
    } catch(Exception ex) {
      throw new RuntimeException("Sparkngin Driver Error", ex) ;
    }
  }
  
  public void send(Message message) throws Exception {
    Timer.Context ctx = mRegistry.timer(HttpSparknginMessageDriver.class.getName(), "send").time() ;
    message.getHeader().setTopic(topic);
    client.sendPost(message, 15000);
    ctx.stop() ;
  }
  
  public void close() { 
    client.close();
  }
}