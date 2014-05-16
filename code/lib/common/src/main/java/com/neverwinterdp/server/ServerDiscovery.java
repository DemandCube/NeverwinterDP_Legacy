package com.neverwinterdp.server;

import java.io.Serializable;
import java.util.List;

import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.service.ServiceDescriptor;

public class ServerDiscovery implements Serializable {
  private ServerState serverState ;
  private ClusterMember member ;
  private List<ServiceDescriptor> services ;
  
  public ServerDiscovery() {
    
  }
  
  public ServerDiscovery(ServerState state, ClusterMember member, List<ServiceDescriptor> services) {
    this.serverState = state ;
    this.member = member ;
    this.services = services ;
  }

  public ServerState getServerState() { return this.serverState ; }
  
  public ClusterMember getMember() {
    return member;
  }

  public List<ServiceDescriptor> getServices() { return services; }
}
