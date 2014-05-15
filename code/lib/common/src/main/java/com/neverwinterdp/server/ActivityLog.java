package com.neverwinterdp.server;

import java.io.Serializable;

public class ActivityLog implements Serializable {
  private String activity;
  private long   exectuteStart;
  private long   executeEnd;
  private String log;

  public ActivityLog() {} 
  
  public ActivityLog(String activity, long start, long end, String log) {
    this.activity = activity ;
    this.exectuteStart = start ;
    this.executeEnd = end ;
    this.log = log ;
  }
  
  public String getActivity() {
    return activity;
  }

  public void setActivity(String command) {
    this.activity = command;
  }

  public long getExectuteStart() {
    return exectuteStart;
  }

  public void setExectuteStart(long exectuteStart) {
    this.exectuteStart = exectuteStart;
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

}
