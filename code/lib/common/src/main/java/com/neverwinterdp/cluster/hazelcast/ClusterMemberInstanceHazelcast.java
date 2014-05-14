package com.neverwinterdp.cluster.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.neverwinterdp.cluster.AbstractClusterMemberInstance;
import com.neverwinterdp.cluster.ClusterMember;
import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.command.Result;
import com.neverwinterdp.cluster.service.ServiceCommand;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class ClusterMemberInstanceHazelcast extends AbstractClusterMemberInstance {
  
  private HazelcastInstance hzinstance ;
  
  public void onInit() {
    super.onInit(); 
    Config config = new XmlConfigBuilder().build();
    config.setProperty("hazelcast.operation.call.timeout.millis", "5000");
    hzinstance = Hazelcast.newHazelcastInstance(config);
    Member member= hzinstance.getCluster().getLocalMember() ;
    setMember(new ClusterMemberImpl(member, getConfig())) ;
  }
  
  public void onDestroy() {
    super.onDestroy() ; 
  }
  
  public <T> T execute(ServiceCommand<T> command) {
    return null ;
  }
  
  public <T> Result<T> execute(Command<T> command, ClusterMember member) {
    IExecutorService exService = hzinstance.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> Result<T>[] execute(Command<T> command, ClusterMember[] member) {
    IExecutorService exService = hzinstance.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
}