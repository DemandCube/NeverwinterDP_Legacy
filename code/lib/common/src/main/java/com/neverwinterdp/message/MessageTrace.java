package com.neverwinterdp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class MessageTrace {
  final static public SimpleDateFormat COMPACT_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss")  ;
  
  private String host      ;
  private String serviceId ;
  private float  serviceVersion ;
  private long   processTime ;
  private String message ;
  
  public MessageTrace() {} 
  
  public MessageTrace(String serviceId, String message) {
    this.serviceId = serviceId ;
    this.processTime = System.currentTimeMillis() ;
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
  
  public float getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(float serviceVersion) {
    this.serviceVersion = serviceVersion;
  }

  public long getProcessTime() {
    return processTime;
  }

  public void setProcessTime(long time) {
    this.processTime = time;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public String toString() {
    StringBuilder b = new StringBuilder("  ") ;
    b.append(COMPACT_DATE_TIME.format(new Date(processTime))).append(' ').
      append(host).append(' ').
      append(serviceId).append('-').append(serviceVersion).
      append(message) ;
    return b.toString() ;
  }
}