package com.neverwinterdp.queue ;



public interface JSONMessageConsumerHandler<T> {
  public void onJSONMessage(JSONMessage<T> jsonMessage)  ;
}