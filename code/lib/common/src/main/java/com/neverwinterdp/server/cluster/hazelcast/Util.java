package com.neverwinterdp.server.cluster.hazelcast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.command.Command;
import com.neverwinterdp.server.command.CommandResult;

class Util {
  final static String HAZELCAST_EXECUTOR_NAME = "default" ;
  
  static <T> CommandResult<T> submit(IExecutorService exService, Command<T> command, ClusterMember member) {
    Member hzmember = ((ClusterMemberImpl) member).getHazelcastMember() ;
    CommandWrapper<T> wrapper = new CommandWrapper<T>(command) ;
    Future<T> future = exService.submitToMember(wrapper, hzmember) ;
    CommandResult<T> result = new CommandResult<T>() ;
    try {
      T ret = future.get(command.getTimeout(), TimeUnit.MILLISECONDS) ;
      result.setResult(ret);
      return result ;
    } catch (InterruptedException | ExecutionException | TimeoutException error) {
      result.setError(error) ;
      return result ;
    }
  }
  
  static <T> CommandResult<T>[] submit( IExecutorService exService, Command<T> command, ClusterMember[] member) {
    List<Member> hzmembers = new ArrayList<Member>() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = ((ClusterMemberImpl) member[i]).getHazelcastMember() ;
      hzmembers.add(hzmember) ;
    }
    CommandWrapper<T> wrapper = new CommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToMembers(wrapper, hzmembers) ;
    CommandResult<T>[] results = new CommandResult[member.length] ;
    long startTime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = ((ClusterMemberImpl) member[i]).getHazelcastMember() ;
      Future<T> future = futures.get(hzmember) ;
      results[i] = new CommandResult<T>() ;
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
  
  static <T> CommandResult<T>[] submit( IExecutorService exService, Command<T> command) {
    CommandWrapper<T> wrapper = new CommandWrapper<T>(command) ;
    Map<Member, Future<T>>  futures = exService.submitToAllMembers(wrapper) ;
    CommandResult<T>[] results = new CommandResult[futures.size()] ;
    long ctime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    Iterator<Map.Entry<Member, Future<T>>> i = futures.entrySet().iterator() ;
    int idx  = 0 ;
    while(i.hasNext()) {
      Map.Entry<Member, Future<T>> entry = i.next() ;
      Member hzmember = entry.getKey() ;
      Future<T> future = entry.getValue() ;
      results[idx] = new CommandResult<T>() ;
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
