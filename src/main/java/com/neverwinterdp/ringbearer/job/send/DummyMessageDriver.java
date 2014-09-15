package com.neverwinterdp.ringbearer.job.send;

import java.util.List;
import java.util.Map;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.yara.MetricRegistry;
import com.neverwinterdp.yara.Timer;

public class DummyMessageDriver implements MessageDriver {
  MetricRegistry mRegistry ;
  private int count ;
  
  public DummyMessageDriver(MetricRegistry mRegistry) {
    this.mRegistry   = mRegistry;
  }
  
  public void init(Map<String, String> props, List<String> connect, String topic) {
  }
  
  public void send(Message message) throws Exception {
    Timer.Context ctx = mRegistry.timer(DummyMessageDriver.class.getSimpleName(), "send").time() ;
    count++ ;
    if(count > 0 &&count % 1000 == 0) {
      System.out.println("Sent  " + count + " messages") ;
    }
    ctx.stop() ;
  }
  
  public void close() { 
    System.out.println("Sent  " + count + " messages") ;
  }

}
