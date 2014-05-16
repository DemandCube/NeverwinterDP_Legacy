package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.neverwinterdp.server.ActivityLog;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.command.ActivityLogsCommand;
import com.neverwinterdp.server.command.CommandResult;
import com.neverwinterdp.server.command.ServerCommand;

public class ClusterEventUnitTest extends AbstractClusterUnitTest {
  @Test
  public void testShutdownRunningEvent() throws Exception {
    assertServerState(ServerState.RUNNING) ;
    client.execute(new ActivityLogsCommand.Clear()) ;

    ClusterMember targetMember = instance[0].getClusterRPC().getMember() ;
    CommandResult<ServerState> shutdownResult = 
        client.execute(new ServerCommand.Shutdown().setLogEnable(true), targetMember) ;
    assertFalse(shutdownResult.hasError()) ;
    assertEquals(ServerState.SHUTDOWN, shutdownResult.getResult()) ;
    //wait to make sure the event are broadcasted to the other nodes
    Thread.sleep(1000); 
    assertActivityLogs("Server Shutdown Event", ActivityLog.ClusterEvent, "ServerStateChange", "SHUTDOWN", 1) ;

    client.execute(new ActivityLogsCommand.Clear()) ;
    CommandResult<ServerState> startResult = 
        client.execute(new ServerCommand.Start().setLogEnable(true), targetMember) ;
    assertFalse(startResult.hasError()) ;
    assertEquals(ServerState.RUNNING, startResult.getResult()) ;
    //wait to make sure the event are broadcasted to the other nodes
    Thread.sleep(1000); 
    assertActivityLogs("Server Start Event", ActivityLog.ClusterEvent, "ServerStateChange", "RUNNING", 1) ;
  }
}