package com.neverwinterdp.server.cluster.hazelcast;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ActivityLog;
import com.neverwinterdp.server.cluster.ClusterRPC;
import com.neverwinterdp.server.command.Command;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
class CommandWrapper<T> implements Callable<T>, HazelcastInstanceAware, Serializable {
  transient private HazelcastInstance hzInstance ;
  private Command<T> command ;
  
  public CommandWrapper() { }
  
  public CommandWrapper(Command<T> command) {
    this.command = command ;
  }
  
  final public T call() throws Exception {
    long start = 0, end = 0 ;
    if(command.isLogEnable()) start = System.currentTimeMillis() ;
    ClusterRPCHazelcast rpc = ClusterRPCHazelcast.getClusterRPC(hzInstance) ;
    Server server = rpc.getServer() ;
    T result = command.execute(server) ;
    if(command.isLogEnable()) {
      end = System.currentTimeMillis() ;
      server.
        getActivityLogs().
        add(new ActivityLog(command.getClass().getSimpleName(), start, end, null));
    }
    return result ;
  }
  
  public void setHazelcastInstance(HazelcastInstance hzInstance) {
    this.hzInstance = hzInstance ;
  }
}