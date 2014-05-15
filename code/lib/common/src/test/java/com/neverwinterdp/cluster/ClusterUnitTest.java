package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.server.ActivityLogs;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerConfig;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.ClusterClientHazelcast;
import com.neverwinterdp.server.command.CommandResult;
import com.neverwinterdp.server.command.ActivityLogsCommand;
import com.neverwinterdp.server.command.PingServer;

public class ClusterUnitTest {
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
    
    CommandResult<String>[] results = client.execute(new PingServer("ping").setLogEnable(true)) ;
    for(CommandResult<String> sel : results) {
      assertFalse(sel.hasError()) ;
      assertEquals("Got your message 'ping'", sel.getResult()) ;
    }    
  }
  
  @After
  public void teardown() {
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    client.shutdown(); 
  }
  
  @Test
  public void testActivityLogs() throws Exception {
    CommandResult<ActivityLogs>[] results = 
      client.execute(new ActivityLogsCommand.Get().setLogEnable(true)) ;
    for(CommandResult<ActivityLogs> sel : results) {
      assertFalse(sel.hasError()) ;
      ActivityLogs activityLogs = sel.getResult() ;
      assertEquals(1, activityLogs.find("Init").size()) ;
      assertEquals(1, activityLogs.find("PingServer").size()) ;
    }
  }
}