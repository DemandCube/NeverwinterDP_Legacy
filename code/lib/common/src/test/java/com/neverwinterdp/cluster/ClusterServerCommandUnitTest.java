package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.neverwinterdp.server.ActivityLog;
import com.neverwinterdp.server.ActivityLogs;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.command.ActivityLogsCommand;
import com.neverwinterdp.server.command.CommandResult;
import com.neverwinterdp.server.command.ServerCommand;

public class ClusterServerCommandUnitTest extends AbstractClusterUnitTest {
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
}