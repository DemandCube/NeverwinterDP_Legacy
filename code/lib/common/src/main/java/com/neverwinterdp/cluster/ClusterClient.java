package com.neverwinterdp.cluster;

import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.command.Result;

public interface ClusterClient {
  public <T> Result<T> execute(Command<T> command, ClusterMember member) ;
  
  public <T> Result<T>[] execute(Command<T> command, ClusterMember[] member) ;
}
