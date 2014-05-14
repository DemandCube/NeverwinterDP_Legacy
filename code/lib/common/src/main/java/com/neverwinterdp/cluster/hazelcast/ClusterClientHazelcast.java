package com.neverwinterdp.cluster.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.neverwinterdp.cluster.ClusterClient;
import com.neverwinterdp.cluster.ClusterMember;
import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.command.Result;

public class ClusterClientHazelcast implements ClusterClient {
  private HazelcastInstance hzclient ;
  
  public ClusterClientHazelcast(String connectUrl) {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress(connectUrl);
    hzclient = HazelcastClient.newHazelcastClient(clientConfig);
  }

  public <T> Result<T> execute(Command<T> command, ClusterMember member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> Result<T>[] execute(Command<T> command, ClusterMember[] member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
}
