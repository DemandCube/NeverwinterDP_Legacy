package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface Cluster {
  final static public String CLUSTER_EVENT_TOPIC = "NeverwinterDP/ClusterEvent" ;
  final static public String CLUSTER_REGISTRATON = "NeverwinterDP/ClusterRegistration" ;
  
  public void onInit(Server server) ;
  
  public void onDestroy(Server server) ;
  
  /**
   * The server descriptor contain the information such hostname, listen port, the state of the server,
   * and the available services that the server provide with the service running status. 
   */
  public ClusterMember getMember() ;
  
  public ClusterRegistraton getClusterRegistration() ;
  
  public void addClusterListener(ClusterListener<Server> listener) ;
  
  /**
   * This method is designed to run certain command on a service such start, stop, ping to check 
   * the state of the service. 
   * 1. The method should find the the registered service by the service descriptor in the command
   * 2. Call the method command.execute(server, service) method.
   * 3. Handle the exception such service not found or the command execute throw an exception.
   * 4. This method cannot be called only if the server state is RUNNING.
   * @param command
   * @return
   */
  public <T> ServiceCommandResult<T> execute(ServiceCommand<T> command, ClusterMember member) ;
  
  public <T> ServiceCommandResult<T>[] execute(ServiceCommand<T> command, ClusterMember[] member) ;
  
  public <T> ServiceCommandResult<T>[] execute(ServiceCommand<T> command) ;
  
  /**
   * This method is designed to be called by the cluster only. When the server is in the 
   * SHUTDOWN state, the cluster service is still functioned and listen to the cluster command
   * @param command
   * @return
   */
  public <T> ServerCommandResult<T> execute(ServerCommand<T> command, ClusterMember member) ;
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command, ClusterMember[] member) ;
  
  public <T> ServerCommandResult<T>[] execute(ServerCommand<T> command) ;
  
  public void broadcast(ClusterEvent event) ;
}