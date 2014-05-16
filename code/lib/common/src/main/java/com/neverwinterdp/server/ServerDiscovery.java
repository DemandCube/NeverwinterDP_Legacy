package com.neverwinterdp.server;

import java.io.Serializable;
import java.util.List;

import com.neverwinterdp.server.service.ServiceDescriptor;

public class ServerDiscovery implements Serializable {
  private ServerState serverState ;
  private List<ServiceDescriptor> services ;
  
  public ServerDiscovery() {
    
  }
  
  public ServerDiscovery(ServerState state, List<ServiceDescriptor> services) {
    this.serverState = state ;
    this.services = services ;
  }

  public ServerState getServerState() { return this.serverState ; }
  
  public List<ServiceDescriptor> getServices() { return services; }
}
