package com.neverwinterdp.server.cluster.hazelcast;

import java.io.Serializable;
import java.util.Set;

import com.hazelcast.core.Member;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.config.ServerConfig;

class ClusterMemberImpl implements ClusterMember, Serializable {
  final static public String VERSION = "version" ; 
  final static public String DESCRIPTION = "description" ; 

  private float  version ;
  private String uuid ;
  private String hostname ;
  private String ipAddress ;
  private int    port ;
  private String description ;
  
  public ClusterMemberImpl() {
  }

  ClusterMemberImpl(Member member, ServerConfig config) {
    this.version = 0f ;
    this.uuid = member.getUuid() ;
    this.hostname = member.getSocketAddress().getHostName() ;
    this.ipAddress = member.getSocketAddress().getAddress().getHostAddress() ; 
    this.port = member.getSocketAddress().getPort() ;
  }

  public String getUuid() { return uuid ; }
  
  public String getHost() { return hostname ; }
  
  public String getIpAddress() { return ipAddress ; }

  public int getPort() { return this.port  ; }

  public float getVersion() { return version ; }

  public Set<String> getRoles() {
    return null;
  }

  public String getDescription() { return description ; }
  
  public int hashCode() {
    if(uuid == null) return 17 ;
    return uuid.hashCode() ;
  }
  
  public boolean equals(Object other) {
    if(uuid == null) return false ;
    return uuid.equals(((ClusterMemberImpl) other).getUuid()) ;
  }
  
  public String toString() {
    return getIpAddress() +  ":" + getPort() ;
  }
}
