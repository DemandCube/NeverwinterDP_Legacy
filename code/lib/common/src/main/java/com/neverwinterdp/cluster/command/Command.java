package com.neverwinterdp.cluster.command;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
abstract public class Command<T> implements Callable<T>, HazelcastInstanceAware, Serializable {
  transient private HazelcastInstance hazelcastInstance ;
  
  private long timeout = 5000 ;
  
  public Command() {
    
  }
  
  public long getTimeout() { return timeout ; }
  public void setTimeout(long timeout) {
    this.timeout = timeout ;
  }
  
  abstract public T execute()  throws Exception ;

  final public T call() throws Exception {
    return execute() ;
  }
  
  public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
    this.hazelcastInstance = hazelcastInstance ;
  }
}