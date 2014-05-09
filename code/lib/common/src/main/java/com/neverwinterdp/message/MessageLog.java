package com.neverwinterdp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageLog {
  final static public SimpleDateFormat COMPACT_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss")  ;
  private String host ;
  private String serviceId ;
  private long   time ;
  private String message ;
  
  public MessageLog() {} 
  
  public MessageLog(String serviceId, String message) {
    this.serviceId = serviceId ;
    this.time = System.currentTimeMillis() ;
    this.message = message ;
  }
  
  public String getHost() { return host; }
  public void setHost(String host) { this.host = host; }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public String toString() {
    StringBuilder b = new StringBuilder("  ") ;
    b.append(serviceId).
      append("[").
      append(COMPACT_DATE_TIME.format(new Date(time))).
      append("] ").
      append(message) ;
    return b.toString() ;
  }
}