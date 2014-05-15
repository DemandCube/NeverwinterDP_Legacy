package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.command.Command;
import com.neverwinterdp.server.command.CommandResult;

public interface ClusterClient {
  public void addClusterListener(ClusterListener<ClusterClient> listener) ;
  public void broadcast(ClusterEvent event) ; 
  
  public <T> CommandResult<T> execute(Command<T> command, ClusterMember member) ;
  
  public <T> CommandResult<T>[] execute(Command<T> command, ClusterMember[] member) ;
  
  public <T> CommandResult<T>[] execute(Command<T> command) ;
  
  public void shutdown() ;
}
