package com.neverwinterdp.server.cluster.hazelcast;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;

class Util {
  final static String HAZELCAST_EXECUTOR_NAME = "default" ;
  static <T> ServiceCommandResult<T> submit(HazelcastInstance instance, ServiceCommand<T> command, ClusterMember member) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    HazelcastMemberSelector selector = new HazelcastMemberSelector(instance) ;
    Member hzmember = selector.select(member) ;
    ServiceCommandWrapper<T> wrapper = new ServiceCommandWrapper<T>(command) ;
    Future<T> future = exService.submitToMember(wrapper, hzmember) ;
    ServiceCommandResult<T> result = new ServiceCommandResult<T>() ;
    try {
      T ret = future.get(command.getTimeout(), TimeUnit.MILLISECONDS) ;
      result.setResult(ret);
      return result ;
    } catch (InterruptedException | ExecutionException | TimeoutException error) {
      result.setError(error) ;
      return result ;
    }
  }
  
  static <T> ServiceCommandResult<T>[] submit(HazelcastInstance instance, ServiceCommand<T> command, ClusterMember[] member) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    HazelcastMemberSelector selector = new HazelcastMemberSelector(instance) ;
    List<Member> hzmembers = selector.selectAsList(member) ; 
    ServiceCommandWrapper<T> wrapper = new ServiceCommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToMembers(wrapper, hzmembers) ;
    ServiceCommandResult<T>[] results = new ServiceCommandResult[member.length] ;
    long startTime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = hzmembers.get(i) ;
      Future<T> future = futures.get(hzmember) ;
      results[i] = new ServiceCommandResult<T>() ;
      try {
        T ret = future.get(waitTime, TimeUnit.MILLISECONDS) ;
        results[i].setResult(ret);
      } catch (InterruptedException | ExecutionException | TimeoutException error) {
        results[i].setError(error) ;
      }
      waitTime = command.getTimeout() - (System.currentTimeMillis() - startTime) ;
    }
    return results ;
  }
  
  
  static <T> ServiceCommandResult<T>[] submit(HazelcastInstance instance, ServiceCommand<T> command) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    ServiceCommandWrapper<T> wrapper = new ServiceCommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToAllMembers(wrapper) ;
    ServiceCommandResult<T>[] results = new ServiceCommandResult[futures.size()] ;
    long ctime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    Iterator<Map.Entry<Member, Future<T>>> i = futures.entrySet().iterator() ;
    int idx  = 0 ;
    while(i.hasNext()) {
      Map.Entry<Member, Future<T>> entry = i.next() ;
      Member hzmember = entry.getKey() ;
      Future<T> future = entry.getValue() ;
      results[idx] = new ServiceCommandResult<T>() ;
      try {
        T ret = future.get(waitTime, TimeUnit.MILLISECONDS) ;
        results[idx].setResult(ret);
      } catch (InterruptedException | ExecutionException | TimeoutException error) {
        results[idx].setError(error) ;
      }
      waitTime = command.getTimeout() - (System.currentTimeMillis() - ctime) ;
      idx++ ;
    }
    return results ;
  }
  
  static <T> ServerCommandResult<T> submit(HazelcastInstance instance, ServerCommand<T> command, ClusterMember member) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    HazelcastMemberSelector selector = new HazelcastMemberSelector(instance) ;
    Member hzmember = selector.select(member) ;
    ServerCommandWrapper<T> wrapper = new ServerCommandWrapper<T>(command) ;
    Future<T> future = exService.submitToMember(wrapper, hzmember) ;
    ServerCommandResult<T> result = new ServerCommandResult<T>() ;
    try {
      T ret = future.get(command.getTimeout(), TimeUnit.MILLISECONDS) ;
      result.setResult(ret);
      return result ;
    } catch (InterruptedException | ExecutionException | TimeoutException error) {
      result.setError(error) ;
      return result ;
    }
  }
  
  static <T> ServerCommandResult<T>[] submit(HazelcastInstance instance, ServerCommand<T> command, ClusterMember[] member) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    HazelcastMemberSelector selector = new HazelcastMemberSelector(instance) ;
    
    List<Member> hzmembers = selector.selectAsList(member) ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = hzmembers.get(i) ;
      hzmembers.add(hzmember) ;
    }
    ServerCommandWrapper<T> wrapper = new ServerCommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToMembers(wrapper, hzmembers) ;
    ServerCommandResult<T>[] results = new ServerCommandResult[member.length] ;
    long startTime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = hzmembers.get(i)  ;
      Future<T> future = futures.get(hzmember) ;
      results[i] = new ServerCommandResult<T>() ;
      try {
        T ret = future.get(waitTime, TimeUnit.MILLISECONDS) ;
        results[i].setResult(ret);
      } catch (InterruptedException | ExecutionException | TimeoutException error) {
        results[i].setError(error) ;
      }
      waitTime = command.getTimeout() - (System.currentTimeMillis() - startTime) ;
    }
    return results ;
  }
  
  static <T> ServerCommandResult<T>[] submit(HazelcastInstance instance,  ServerCommand<T> command) {
    IExecutorService exService = instance.getExecutorService(HAZELCAST_EXECUTOR_NAME) ;
    ServerCommandWrapper<T> wrapper = new ServerCommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToAllMembers(wrapper) ;
    ServerCommandResult<T>[] results = new ServerCommandResult[futures.size()] ;
    long ctime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    Iterator<Map.Entry<Member, Future<T>>> i = futures.entrySet().iterator() ;
    int idx  = 0 ;
    while(i.hasNext()) {
      Map.Entry<Member, Future<T>> entry = i.next() ;
      Future<T> future = entry.getValue() ;
      results[idx] = new ServerCommandResult<T>() ;
      try {
        T ret = future.get(waitTime, TimeUnit.MILLISECONDS) ;
        results[idx].setResult(ret);
      } catch (InterruptedException | ExecutionException | TimeoutException error) {
        results[idx].setError(error) ;
      }
      waitTime = command.getTimeout() - (System.currentTimeMillis() - ctime) ;
      idx++ ;
    }
    return results ;
  }
}
