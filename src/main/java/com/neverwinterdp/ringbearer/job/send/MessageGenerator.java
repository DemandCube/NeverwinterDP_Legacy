package com.neverwinterdp.ringbearer.job.send;

import com.neverwinterdp.message.Message;

public class MessageGenerator {
  private long    idTracker = 0;
  private String  idPrefix  = "message";
  private int     messageSize = 200;
  private boolean logEnable = false;  
  
  public void setIdPrefix(String idPrefix) {
    this.idPrefix = idPrefix ;
  }
  
  public void setMessageSize(int size) {
    this.messageSize = size ;
  }
  
  public void setLogEnable(boolean b) {
    this.logEnable = b ;
  }
  
  public Message next() {
    idTracker++ ;
    byte[] data = new byte[messageSize] ;
    Message message = new Message(idPrefix + "-" +idTracker, data, logEnable) ;
    return message ;
  }
}
