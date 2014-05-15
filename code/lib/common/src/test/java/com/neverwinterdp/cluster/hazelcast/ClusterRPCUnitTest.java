package com.neverwinterdp.cluster.hazelcast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerConfig;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.ClusterClientHazelcast;
import com.neverwinterdp.server.command.CommandResult;
import com.neverwinterdp.server.command.PingServer;

public class ClusterRPCUnitTest {
  Server[] instance ;
  ClusterClient client ;
  
  @Before
  public void setup() {
    instance = new Server[3] ;
    ServerConfig config = new ServerConfig() ;
    config.setVersion(1.0f);
    for(int i = 0; i < instance.length; i++) {
      instance[i] = new Server() ;  
      instance[i].setConfig(config);
      instance[i].onInit();
    }
    ClusterMember member = instance[1].getClusterRPC().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new ClusterClientHazelcast(connectUrl) ;
  }
  
  @After
  public void teardown() {
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    client.shutdown(); 
  }
  
  @Test
  public void testPing() throws Exception {
    PingServer ping = new PingServer("ping") ;
    ping.setTimeout(10000l);
    CommandResult<String> result = 
      instance[0].getClusterRPC().execute(ping, instance[1].getClusterRPC().getMember()) ;
    if(result.hasError()) {
      result.getError().printStackTrace() ;
    }
    assertFalse(result.hasError()) ;
    assertEquals("Got your message 'ping'", result.getResult()) ;
    
    ClusterMember[] allMember = new ClusterMember[instance.length] ;
    for(int i = 0 ; i < allMember.length; i++) {
      allMember[i] = instance[i].getClusterRPC().getMember() ;
    }
    CommandResult<String>[] results = client.execute(ping, allMember) ;
    for(CommandResult<String> sel : results) {
      assertFalse(sel.hasError()) ;
      assertEquals("Got your message 'ping'", sel.getResult()) ;
    }
    
  }
}
