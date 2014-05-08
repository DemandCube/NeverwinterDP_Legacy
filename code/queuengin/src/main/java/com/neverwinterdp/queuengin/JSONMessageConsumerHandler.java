package com.neverwinterdp.queuengin ;



public interface JSONMessageConsumerHandler<T> {
  public void onJSONMessage(JSONMessage<T> jsonMessage)  ;
}