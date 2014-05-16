package com.neverwinterdp.server.cluster.hazelcast;

import java.io.Serializable;
import java.util.Set;

import com.hazelcast.core.Member;
import com.neverwinterdp.server.ServerConfig;
import com.neverwinterdp.server.cluster.ClusterMember;

class ClusterMemberImpl implements ClusterMember, Serializable {
  final static public String VERSION = "version" ; 
  final static public String DESCRIPTION = "description" ; 
      
  private Member member ;
  
  public ClusterMemberImpl() {
    
  }
  
  ClusterMemberImpl(Member member) {
    this.member = member ;
  }
  
  ClusterMemberImpl(Member member, ServerConfig config) {
    this.member = member ;
    this.member.setFloatAttribute(VERSION, config.getVersion());
  }

  public String getId() { return member.getUuid() ; }
  
  public String getHost() {
    return member.getSocketAddress().getHostName() ;
  }
  
  public String getIpAddress() {
    return member.getSocketAddress().getAddress().getHostAddress() ;
  }

  public int getPort() {
    return member.getSocketAddress().getPort() ;
  }

  public float getVersion() {
    return member.getFloatAttribute(VERSION);
  }

  public Set<String> getRoles() {
    return null;
  }

  public String getDescription() {
    return member.getStringAttribute(DESCRIPTION) ;
  }
  
  public Member getHazelcastMember() { return this.member ; }
  
  public String toString() {
    return getIpAddress() +  ":" + getPort() ;
  }
}
