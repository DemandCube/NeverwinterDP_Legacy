package com.neverwinterdp.cluster.hazelcast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.HazelcastClusterClient;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServerCommands;
import com.neverwinterdp.server.config.ServerConfig;

public class HazelcastClusterUnitTest {
  static Server[] instance ;
  static ClusterClient client ;
  
  @BeforeClass
  static public void setup() {
    instance = new Server[3] ;
    ServerConfig config = new ServerConfig() ;
    config.setVersion(1.0f);
    for(int i = 0; i < instance.length; i++) {
      instance[i] = new Server() ;  
      instance[i].setConfig(config);
      instance[i].onInit();
      instance[i].start();
    }
    ClusterMember member = instance[1].getCluster().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new HazelcastClusterClient(connectUrl) ;
  }
  
  @AfterClass
  static public void teardown() {
    client.shutdown();
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    client.shutdown(); 
  }
  
  @Test
  public void testPing() throws Exception {
    ServerCommand<ServerState> ping = new ServerCommands.Ping() ;
    ping.setTimeout(10000l);
    ClusterMember targetMember = instance[1].getCluster().getMember() ;
    ServerCommandResult<ServerState> result = instance[0].getCluster().execute(ping, targetMember) ;
    if(result.hasError()) {
      result.getError().printStackTrace() ;
    }
    assertFalse(result.hasError()) ;
    assertEquals(ServerState.RUNNING, result.getResult()) ;
    
    ClusterMember[] allMember = new ClusterMember[instance.length] ;
    for(int i = 0 ; i < allMember.length; i++) {
      allMember[i] = instance[i].getCluster().getMember() ;
    }
    ServerCommandResult<ServerState>[] results = client.execute(ping, allMember) ;
    for(ServerCommandResult<ServerState> sel : results) {
      assertFalse(sel.hasError()) ;
      assertEquals(ServerState.RUNNING, sel.getResult()) ;
    }
    
  }
}
