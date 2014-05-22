package com.neverwinterdp.server.cluster;


public interface ClusterListener<T> {
  public void onEvent(T listener, ClusterEvent event) ;
}
