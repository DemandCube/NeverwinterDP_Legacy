package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Assert;

import com.neverwinterdp.server.ActivityLog;
import com.neverwinterdp.server.ActivityLogs;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.command.ActivityLogsCommand;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServerCommands;

public class Util {
  static void assertServerState(ClusterClient client, ServerState state) {
    ServerCommand<ServerState> ping = new ServerCommands.Ping().setLogEnable(true) ;
    for(ServerCommandResult<ServerState> sel : client.execute(ping)) {
      assertFalse(sel.hasError()) ;
      assertEquals(state, sel.getResult()) ;
    }
  }

  static void assertActivityLogs(ClusterClient client, String header, ActivityLog.Type type, String activity, String logExp, int expect) throws Exception {
    System.out.println("\nAssert " + header + "(Expect " + expect + " for each server)");
    System.out.println("---------------------------------------------------------------------");
    ServerCommandResult<ActivityLogs>[] results = 
        client.execute(new ActivityLogsCommand.Get().setLogEnable(true)) ;
    for(ServerCommandResult<ActivityLogs> sel : results) {
      assertFalse(sel.hasError()) ;
      ActivityLogs activityLogs = sel.getResult() ;
      List<ActivityLog> logs = activityLogs.find(type, activity) ;
      assertEquals(expect, logs.size()) ;
      for(ActivityLog log : logs) {
        Assert.assertTrue(log.getLog().indexOf(logExp) >= 0) ;
        System.out.println(log + "(OK)");
      }
    }
    System.out.println("---------------------------------------------------------------------\n");
  }
}
