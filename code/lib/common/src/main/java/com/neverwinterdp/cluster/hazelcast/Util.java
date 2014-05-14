package com.neverwinterdp.cluster.hazelcast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.neverwinterdp.cluster.ClusterMember;
import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.command.Result;

class Util {
  final static String HAZELCAST_EXECUTOR_NAME = "default" ;
  
  static <T> Result<T> submit(IExecutorService exService, Command<T> command, ClusterMember member) {
    Member hzmember = ((ClusterMemberImpl) member).getHazelcastMember() ;
    Future<T> future = exService.submitToMember(command, hzmember) ;
    Result<T> result = new Result<T>() ;
    try {
      T ret = future.get(command.getTimeout(), TimeUnit.MICROSECONDS) ;
      result.setResult(ret);
      return result ;
    } catch (InterruptedException | ExecutionException | TimeoutException error) {
      result.setError(error) ;
      return result ;
    }
  }
  
  static <T> Result<T>[] submit( IExecutorService exService, Command<T> command, ClusterMember[] member) {
    List<Member> hzmembers = new ArrayList<Member>() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = ((ClusterMemberImpl) member[i]).getHazelcastMember() ;
      hzmembers.add(hzmember) ;
    }
    Map<Member, Future<T>>  futures = exService.submitToMembers(command, hzmembers) ;
    Result<T>[] results = new Result[member.length] ;
    long ctime = System.currentTimeMillis() ;
    long waitTime = command.getTimeout() ;
    for(int i = 0; i < member.length; i++) {
      Member hzmember = ((ClusterMemberImpl) member[i]).getHazelcastMember() ;
      Future<T> future = futures.get(hzmember) ;
      results[i] = new Result<T>() ;
      try {
        T ret = future.get(waitTime, TimeUnit.MICROSECONDS) ;
        results[i].setResult(ret);
      } catch (InterruptedException | ExecutionException | TimeoutException error) {
        results[i].setError(error) ;
      }
      waitTime = command.getTimeout() - (System.currentTimeMillis() - ctime) ;
    }
    return results ;
  }
}
