package com.neverwinterdp.cluster;

import java.util.Set;

public interface ClusterMember {
  public String getHost() ;
  public String getIpAddress() ;
  public int getPort() ;
  public float getVersion() ;
  public String getState() ;
  public Set<String> getRoles() ;
  public String getDescription() ;
}
