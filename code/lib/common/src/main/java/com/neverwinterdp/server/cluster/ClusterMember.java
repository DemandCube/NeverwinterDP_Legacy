package com.neverwinterdp.server.cluster;

import java.io.Serializable;
import java.util.Set;

import com.neverwinterdp.server.ServerState;

public interface ClusterMember {
  public String getHost() ;
  public String getIpAddress() ;
  public int getPort() ;
  public float getVersion() ;
  public ServerState getState() ;
  public void   setState(ServerState state) ;
  public Set<String> getRoles() ;
  public String getDescription() ;
}
