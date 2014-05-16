package com.neverwinterdp.server.cluster.hazelcast;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.neverwinterdp.server.ServerDiscovery;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterEvent;
import com.neverwinterdp.server.cluster.ClusterListener;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.ClusterRPC;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServerCommands;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;

public class ClusterClientHazelcast implements ClusterClient,  MessageListener<ClusterEvent>  {
  private HazelcastInstance hzclient ;
  private List<ClusterListener<ClusterClient>> listeners = new ArrayList<ClusterListener<ClusterClient>>() ;
  private ITopic<ClusterEvent> clusterEventTopic ;
  private String               clusterEventTopicListenerId ;
  
  public ClusterClientHazelcast(String connectUrl) {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress(connectUrl);
    hzclient = HazelcastClient.newHazelcastClient(clientConfig);
    clusterEventTopic = hzclient.getTopic(ClusterRPC.CLUSTER_EVENT_TOPIC);
    clusterEventTopicListenerId = clusterEventTopic.addMessageListener(this) ;
  }
  
  public void addClusterListener(ClusterListener<ClusterClient> listener) {
    listeners.add(listener) ;
  }
  
  public ServerDiscovery getServerDiscovery(ClusterMember member) {
    ServerCommand<ServerDiscovery> cmd = new ServerCommands.GetServerDiscovery() ;
    ServerCommandResult<ServerDiscovery> result = execute(cmd, member) ;
    return result.getResult() ;
  }
  
  public <T> ServiceCommandResult<T>  execute(ServiceCommand<T> command, ClusterMember member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> ServiceCommandResult<T>[] execute(ServiceCommand<T> command, ClusterMember[] member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> ServiceCommandResult<T> [] execute(ServiceCommand<T> command) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command) ;
  }

  public <T> ServerCommandResult<T> execute(ServerCommand<T> command, ClusterMember member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command, ClusterMember[] member) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command, member) ;
  }
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command) {
    IExecutorService exService = hzclient.getExecutorService(Util.HAZELCAST_EXECUTOR_NAME);
    return Util.submit(exService, command) ;
  }
  
  public void broadcast(ClusterEvent event) {
    clusterEventTopic.publish(event);
  }

  public void onMessage(Message<ClusterEvent> message) {
    ClusterEvent event = message.getMessageObject() ;
    for(int i = 0; i < listeners.size(); i++) {
      ClusterListener<ClusterClient> listener = listeners.get(i) ;
      listener.onEvent(this, event) ;
    }
    System.out.println("Client got and event: " + event);
  }
  
  public void shutdown() {
    clusterEventTopic.removeMessageListener(clusterEventTopicListenerId) ;
    hzclient.shutdown(); 
  }
}
