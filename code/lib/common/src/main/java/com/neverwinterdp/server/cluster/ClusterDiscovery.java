package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.ServerDiscovery;

public interface ClusterDiscovery {
  public ServerDiscovery   getServerDiscovery(ClusterMember member) ;
  public ServerDiscovery[] getServerDiscovery() ;
  public void update(ClusterMember member, ServerDiscovery entry) ;
}