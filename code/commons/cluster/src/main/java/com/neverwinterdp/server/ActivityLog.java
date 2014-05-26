package com.neverwinterdp.server;

import java.io.Serializable;

public class ActivityLog implements Serializable {
  static public enum Type { Auto, Command, ClusterEvent }
  final static public Type Command = Type.Command ;
  final static public Type ClusterEvent = Type.ClusterEvent ;
  
  private String activity;
  private Type   type ;
  private long   executeStart;
  private long   executeEnd;
  private String log;

  public ActivityLog() {} 
  
  public ActivityLog(String activity, Type type, long start, long end, String log) {
    this.activity = activity ;
    this.type = type ;
    this.executeStart = start ;
    this.executeEnd = end ;
    this.log = log ;
  }
  
  public String getActivity() {
    return activity;
  }

  public void setActivity(String command) {
    this.activity = command;
  }
  
  public Type getType() { return type; }
  public void setType(Type type) {
    this.type = type;
  }

  public long getExecuteStart() {
    return executeStart;
  }

  public void setExecuteStart(long exectuteStart) {
    this.executeStart = exectuteStart;
  }

  public long getExecuteEnd() {
    return executeEnd;
  }

  public void setExecuteEnd(long executeEnd) {
    this.executeEnd = executeEnd;
  }

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }
  
  public long getExecuteTime() { return executeEnd - executeStart ; }
  
  public String toString() {
    StringBuilder b = new StringBuilder() ;
    b.append(type).append(" ").
      append(activity).append('(').append(getExecuteTime()).append("ms): ").
      append(log) ;
    return b.toString() ;
  }
}
