package com.neverwinterdp.server.cluster.hazelcast;

import com.hazelcast.core.IMap;
import com.neverwinterdp.server.ServerRegistration;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.ClusterRegistraton;

public class ClusterRegistrationImpl extends ClusterRegistraton {
  private IMap<String, ServerRegistration> registrationMap ;
  
  public ClusterRegistrationImpl(IMap<String, ServerRegistration> map) {
    this.registrationMap = map ;
  }
  
  public ServerRegistration getServerRegistration(ClusterMember member) {
    return registrationMap.get(member.getUuid());
  }

  public ServerRegistration[] getServerRegistration() {
    ServerRegistration[] array = new ServerRegistration[registrationMap.size()] ;
    array = registrationMap.values().toArray(array) ;
    return array ;
  }

  public void update(ServerRegistration registration) {
    String key = registration.getClusterMember().getUuid() ;
    registrationMap.put(key, registration);
    registrationMap.flush() ;
  }
  
  public void remove(ClusterMember member) {
    String key = member.getUuid() ;
    registrationMap.delete(key);
    registrationMap.flush() ;
  }
  
  public int  getNumberOfServers() { return registrationMap.size() ; }
}
