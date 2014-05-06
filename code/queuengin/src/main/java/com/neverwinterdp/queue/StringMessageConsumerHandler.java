package com.neverwinterdp.queue ;


public interface StringMessageConsumerHandler {
  public void onMessage(String key, String message)  ;
}