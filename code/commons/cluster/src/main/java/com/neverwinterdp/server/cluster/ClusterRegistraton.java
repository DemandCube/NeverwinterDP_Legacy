package com.neverwinterdp.server.cluster;

import java.util.HashMap;
import java.util.Map;

import com.neverwinterdp.server.ServerRegistration;
import com.neverwinterdp.server.service.ServiceRegistration;

abstract public class ClusterRegistraton {
  abstract public ServerRegistration   getServerRegistration(ClusterMember member) ;
  abstract public ServerRegistration[] getServerRegistration() ;
  abstract public void update(ServerRegistration registration) ;
  abstract public void remove(ClusterMember member) ;
  abstract public int  getNumberOfServers() ;
  
  public Map<ClusterMember, ServiceRegistration> findByServiceName(String name) {
    Map<ClusterMember, ServiceRegistration> map = new HashMap<ClusterMember, ServiceRegistration>() ;
    for(ServerRegistration sel : getServerRegistration()) {
      ServiceRegistration registration = sel.findByServiceName(name) ;
      if(registration != null) {
        map.put(sel.getClusterMember(), registration) ;
      }
    }
    return map ;
  }
}