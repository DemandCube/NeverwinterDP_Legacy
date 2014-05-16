package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.server.ActivityLog;
import com.neverwinterdp.server.ActivityLogs;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerConfig;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.ClusterClientHazelcast;
import com.neverwinterdp.server.command.ActivityLogsCommand;
import com.neverwinterdp.server.command.Command;
import com.neverwinterdp.server.command.CommandResult;
import com.neverwinterdp.server.command.ServerCommand;

public class ClusterServerCommandUnitTest {
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
    ClusterMember member = instance[1].getClusterRPC().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new ClusterClientHazelcast(connectUrl) ;
  }
  
  @AfterClass
  static public void teardown() {
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    client.shutdown(); 
  }
  
  @Test
  public void testGetActivityLogs() throws Exception {
    assertServerState(ServerState.RUNNING) ;
    
    CommandResult<ActivityLogs>[] results = 
      client.execute(new ActivityLogsCommand.Get().setLogEnable(true)) ;
    for(CommandResult<ActivityLogs> sel : results) {
      assertFalse(sel.hasError()) ;
      ActivityLogs activityLogs = sel.getResult() ;
      assertEquals(1, activityLogs.find(ActivityLog.Command, "Ping").size()) ;
    }
  }
  
  @Test
  public void testClearActivityLogs() throws Exception {
    assertServerState(ServerState.RUNNING) ;
    client.execute(new ActivityLogsCommand.Clear()) ;
    
    CommandResult<ActivityLogs>[] results = client.execute(new ActivityLogsCommand.Get()) ;
    for(CommandResult<ActivityLogs> sel : results) {
      assertFalse(sel.hasError()) ;
      ActivityLogs activityLogs = sel.getResult() ;
      assertEquals(0, activityLogs.size()) ;
    }
  }
  
  @Test
  public void testStartShutDown() throws Exception {
    assertServerState(ServerState.RUNNING) ;
    client.execute(new ActivityLogsCommand.Clear()) ;
    
    CommandResult<ServerState>[] shutdownResults = 
      client.execute(new ServerCommand.Shutdown().setLogEnable(true)) ;
    for(CommandResult<ServerState> sel : shutdownResults) {
      assertFalse(sel.hasError()) ;
      ServerState serverState = sel.getResult() ;
      assertEquals(ServerState.SHUTDOWN, serverState) ;
    }
    assertServerState(ServerState.SHUTDOWN) ;
    
    CommandResult<ServerState>[] startResults = 
      client.execute(new ServerCommand.Start().setLogEnable(true)) ;
    for(CommandResult<ServerState> sel : startResults) {
      assertFalse(sel.hasError()) ;
      ServerState serverState = sel.getResult() ;
      assertEquals(ServerState.RUNNING, serverState) ;
    }
    assertServerState(ServerState.RUNNING) ;
  }
  
  void assertServerState(ServerState state) {
    Command<ServerState> ping = new ServerCommand.Ping().setLogEnable(true) ;
    for(CommandResult<ServerState> sel : client.execute(ping)) {
      assertFalse(sel.hasError()) ;
      assertEquals(state, sel.getResult()) ;
    }
  }
}