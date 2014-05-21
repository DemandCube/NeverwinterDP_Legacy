package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.Server;

public class ServerStateChangeListener implements ClusterListener<Server> {
  public void onEvent(Server listener, ClusterEvent event) {
    if(event.getType() == ClusterEvent.Type.ServerStateChange) {
      System.out.println("");
    }
  }
}
