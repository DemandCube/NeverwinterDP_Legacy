package com.neverwinterdp.cluster.hazelcast;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.cluster.ClusterClient;
import com.neverwinterdp.cluster.ClusterMember;
import com.neverwinterdp.cluster.ClusterMemberConfig;
import com.neverwinterdp.cluster.ClusterMemberInstance;
import com.neverwinterdp.cluster.command.PingCommand;
import com.neverwinterdp.cluster.command.Result;

public class ClusterMemberInstanceHazelcastUnitTest {
  ClusterMemberInstance[] instance ;
  ClusterClient client ;
  
  @Before
  public void setup() {
    instance = new ClusterMemberInstance[3] ;
    ClusterMemberConfig config = new ClusterMemberConfig() ;
    config.setVersion(1.0f);
    for(int i = 0; i < instance.length; i++) {
      ClusterMemberInstanceHazelcast impl = new ClusterMemberInstanceHazelcast() ;  
      impl.setConfig(config);
      impl.onInit();
      instance[i] = impl ;
    }
    ClusterMember member = instance[1].getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new ClusterClientHazelcast(connectUrl) ;
  }
  
  @After
  public void teardown() {
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
  }
  
  @Test
  public void test() throws Exception {
    PingCommand ping = new PingCommand("ping") ;
    ping.setTimeout(10000l);
    Result<String> result = instance[0].execute(ping, instance[1].getMember()) ;
    assertFalse(result.hasError()) ;
    assertEquals("Got your message 'ping'", result.getResult()) ;
    
    ClusterMember[] allMember = new ClusterMember[instance.length] ;
    for(int i = 0 ; i < allMember.length; i++) {
      allMember[i] = instance[i].getMember() ;
    }
    Result<String>[] results = client.execute(ping, allMember) ;
    for(Result<String> sel : results) {
      assertFalse(sel.hasError()) ;
      assertEquals("Got your message 'ping'", sel.getResult()) ;
    }
  }
}
