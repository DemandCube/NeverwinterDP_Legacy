package com.neverwinterdp.ringbearer.job.send;

import java.util.List;
import java.util.Map;

import com.codahale.metrics.Timer;
import com.neverwinterdp.message.Message;
import com.neverwinterdp.util.monitor.ApplicationMonitor;
import com.neverwinterdp.util.monitor.ComponentMonitor;

public class DummyMessageDriver implements MessageDriver {
  ComponentMonitor driverMonitor ;
  private int count ;
  
  public DummyMessageDriver(ApplicationMonitor appMonitor) {
    driverMonitor   = appMonitor.createComponentMonitor(HttpSparknginMessageDriver.class) ;
  }
  
  public void init(Map<String, String> props, List<String> connect, String topic) {
  }
  
  public void send(Message message) throws Exception {
    Timer.Context ctx = driverMonitor.timer("send(Message)").time() ;
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
