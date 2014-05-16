package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

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

abstract public class AbstractClusterUnitTest {
  static protected Server[]      instance ;
  static protected ClusterClient client ;

  @BeforeClass
  static public void setup() throws Exception {
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
  static public void teardown() throws Exception {
    client.shutdown(); 
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    Thread.sleep(1000);
  }

  protected void assertServerState(ServerState state) {
    Command<ServerState> ping = new ServerCommand.Ping().setLogEnable(true) ;
    for(CommandResult<ServerState> sel : client.execute(ping)) {
      assertFalse(sel.hasError()) ;
      assertEquals(state, sel.getResult()) ;
    }
  }

  protected void assertActivityLogs(String header, ActivityLog.Type type, String activity, String logExp, int expect) throws Exception {
    System.out.println("\nAssert " + header + "(Expect " + expect + " for each server)");
    System.out.println("---------------------------------------------------------------------");
    CommandResult<ActivityLogs>[] results = 
        client.execute(new ActivityLogsCommand.Get().setLogEnable(true)) ;
    for(CommandResult<ActivityLogs> sel : results) {
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