package com.neverwinterdp.cluster.hazelcast;

import java.util.Set;

import com.hazelcast.core.Member;
import com.neverwinterdp.cluster.ClusterMember;
import com.neverwinterdp.cluster.ClusterMemberConfig;

class ClusterMemberImpl implements ClusterMember {
  final static public String VERSION = "version" ; 
  final static public String DESCRIPTION = "description" ; 
      
  private Member member ;
  
  public ClusterMemberImpl() {
    
  }
  
  ClusterMemberImpl(Member member) {
    this.member = member ;
  }
  
  ClusterMemberImpl(Member member, ClusterMemberConfig config) {
    this.member = member ;
    this.member.setFloatAttribute(VERSION, config.getVersion());
  }

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

  public String getState() {
    return null;
  }

  public Set<String> getRoles() {
    return null;
  }

  public String getDescription() {
    return member.getStringAttribute(DESCRIPTION) ;
  }
  
  public Member getHazelcastMember() { return this.member ; }
}
