package com.neverwinterdp.server.command;

import com.neverwinterdp.server.ActivityLogs;
import com.neverwinterdp.server.Server;

public class ActivityLogsCommand {

  static public class Get extends ServerCommand<ActivityLogs> {
    public ActivityLogs execute(Server server) throws Exception {
      return server.getActivityLogs() ;
    } 
  }
  
  static public class Clear extends ServerCommand<Integer> {
    public Integer execute(Server server) throws Exception {
      ActivityLogs logs = server.getActivityLogs();
      int size = logs.getActivityLog().size() ;
      logs.clear(); 
      return size ;
    } 
  }
}
