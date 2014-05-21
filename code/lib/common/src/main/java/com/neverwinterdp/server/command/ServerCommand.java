package com.neverwinterdp.server.command;

import java.io.Serializable;

import com.neverwinterdp.server.Server;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
abstract public class ServerCommand<T> implements Serializable {
  private long    timeout = 5000l ; 
  private boolean logEnable ;
  
  public ServerCommand() {
  }
  
  public long getTimeout() { return timeout ; }
  public ServerCommand<T> setTimeout(long timeout) {
    this.timeout = timeout ;
    return this ;
  }
  
  public boolean isLogEnable() { return logEnable; }
  public ServerCommand<T>    setLogEnable(boolean logEnable) {
    this.logEnable = logEnable;
    return this ;
  }

  abstract public T execute(Server server)  throws Exception ;

  public String getActivityLogName() { return getClass().getSimpleName() ; }
  
  public String getActivityLogMessage() { return null ; }
}