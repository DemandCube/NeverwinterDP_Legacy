package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.ServerDiscovery;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;

public interface ClusterClient {
  public void addClusterListener(ClusterListener<ClusterClient> listener) ;
  
  public void broadcast(ClusterEvent event) ; 
  
  public ServerDiscovery getServerDiscovery(ClusterMember member) ; 
  
  public <T> ServiceCommandResult<T> execute(ServiceCommand<T> command, ClusterMember member) ;
  
  public <T> ServiceCommandResult<T>[] execute(ServiceCommand<T> command, ClusterMember[] member) ;
  
  public <T> ServiceCommandResult<T>[] execute(ServiceCommand<T> command) ;
  
  public <T> ServerCommandResult<T> execute(ServerCommand<T> command, ClusterMember member) ;
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command, ClusterMember[] member) ;
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command) ;
  
  public void shutdown() ;
}
