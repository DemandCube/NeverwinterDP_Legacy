package com.neverwinterdp.server;

import java.io.Serializable;
import java.util.List;

import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.service.ServiceDescriptor;

public class ServerDiscovery implements Serializable {
  private ClusterMember member ;
  private ServiceDescriptor[] services ;
  
  public ServerDiscovery() {
    
  }
  
  public ServerDiscovery(ClusterMember member, ServiceDescriptor[] services) {
    this.member = member ;
    this.services = services ;
  }

  public ClusterMember getMember() {
    return member;
  }

  public ServiceDescriptor[] getServices() {
    return services;
  }
}
