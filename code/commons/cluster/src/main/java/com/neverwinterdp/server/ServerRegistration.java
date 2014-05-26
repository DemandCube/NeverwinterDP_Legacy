package com.neverwinterdp.server;

import java.io.Serializable;
import java.util.List;

import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.service.ServiceRegistration;

public class ServerRegistration implements Serializable {
  private ClusterMember             clusterMember;
  private ServerState               serverState;
  private List<ServiceRegistration> services;

  public ServerRegistration() {
  }

  public ServerRegistration(ClusterMember member, ServerState state, List<ServiceRegistration> services) {
    this.clusterMember = member ;
    this.serverState = state;
    this.services = services;
  }

  public ClusterMember getClusterMember() { return this.clusterMember ; }
  
  public ServerState getServerState() {
    return this.serverState;
  }

  public List<ServiceRegistration> getServices() {
    return services;
  }
  
  public ServiceRegistration findByServiceName(String name) {
    for(int i = 0; i < services.size(); i++) {
      ServiceRegistration registration = services.get(i) ;
      if(name.equals(registration.getName())) return registration ;
    }
    return null ;
  }
}
