package com.neverwinterdp.server.cluster.hazelcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.neverwinterdp.server.cluster.ClusterMember;

public class HazelcastMemberSelector {
  private Map<String, Member> memberMap = new HashMap<String, Member>() ;
  
  public HazelcastMemberSelector(HazelcastInstance instance) {
    for(Member sel : instance.getCluster().getMembers()) {
      memberMap.put(sel.getUuid(), sel) ;
    }
  }
  
  public Member select(ClusterMember cmember) {
    return memberMap.get(cmember.getUuid()) ;
  }
  
  public Member[] select(ClusterMember[] cmember) {
    Member[] member = new Member[cmember.length] ;
    for(int i = 0; i < member.length; i++) {
      member[i] = memberMap.get(cmember[i].getUuid()) ;
    }
    return member ;
  }
  
  public List<Member> selectAsList(ClusterMember[] cmember) {
    List<Member> members = new ArrayList<Member>() ;
    Member[] member = new Member[cmember.length] ;
    for(int i = 0; i < member.length; i++) {
      members.add(memberMap.get(cmember[i].getUuid())) ;
    }
    return members ;
  }
}
