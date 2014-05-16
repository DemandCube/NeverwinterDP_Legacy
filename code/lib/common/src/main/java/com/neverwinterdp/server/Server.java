package com.neverwinterdp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neverwinterdp.server.cluster.ClusterEvent;
import com.neverwinterdp.server.cluster.ClusterRPC;
import com.neverwinterdp.server.cluster.hazelcast.ClusterRPCHazelcast;
import com.neverwinterdp.server.service.Service;
import com.neverwinterdp.server.service.ServiceDescriptor;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * The server can be understood as a single container or a machine that contains an unlimited number
 * of the services.
 */
public class Server {
  private Logger logger ;
  
  private ServerConfig config ;
  private ClusterRPC   clusterRPC   ;
  private Map<String, Service> services = new ConcurrentHashMap<String, Service>() ; 
  private ActivityLogs  activityLogs = new ActivityLogs() ;
  private ServerState serverState = null ;
  /**
   * The configuration for the server such name, group, version, description, listen port. It also
   * may contain the service configurations
   * */
  public ServerConfig getConfig() { return config ; }
  
  public void setConfig(ServerConfig config) { this.config = config ; }
  
  public ClusterRPC getClusterRPC() { return clusterRPC ; }
  
  public ServerState getServerState() { return serverState ; }
  
  public void setServerState(ServerState state) {
    this.serverState = state ;
  }
  
  public ActivityLogs  getActivityLogs() { return this.activityLogs ; }
  
  public ServerDiscovery getServerDiscovery() {
    List<ServiceDescriptor> serviceDescriptors = new ArrayList<ServiceDescriptor>() ;
    for(Service service : services.values()) {
      serviceDescriptors.add(service.getServiceDescriptor()) ;
    }
    return new ServerDiscovery(serverState, null, serviceDescriptors) ;
  }
  
  public Logger getLogger() { return this.logger ; }
  
  public Logger getLogger(String name) { 
    String address = clusterRPC.getMember().toString() ;
    return LoggerFactory.getLogger("[" + address + "][NeverwinterDP] " + name) ;
  }
  
  /**
   * This lifecycle method be called after the configuration is set. The method should:
   * 1. Compute the configuration and add the services with the service configuration.
   * 2. Loop through the services and call service.onInit()
   * 3. Set the state of the services to init.
   * 4. Set the state of the server to init. 
   * 5. Add and configure the cluster services, start the cluster services
   */
  public void onInit() {
    long start = System.currentTimeMillis() ;
    clusterRPC = new ClusterRPCHazelcast() ;
    clusterRPC.onInit(this);
    
    logger = getLogger(getClass().getSimpleName()) ;
    logger.info("Start onInit()");
    
    setServerState(ServerState.INIT);
    long end = System.currentTimeMillis() ;
    activityLogs.add(new ActivityLog("Init", ActivityLog.Type.Auto, start, end, null)) ;
    logger.info("Finish onInit()");
  }
  
  /**
   * This method is called after the stopServices is called. This method should:
   * 1. Stop and destroy all the cluster services
   * 2. Release all the resources if necessary, save the monitor or profile information.
   */
  public void onDestroy() {
    logger.info("Start onDestroy()");
    clusterRPC.onDestroy(this);
    logger.info("Finish onDestroy()");
  }
  
  /**
   * This method lifecycle should be called after the method onInit() is called. 
   * This method should be called by the external service that can access the Server instance 
   * or the cluster services.
   * 
   * This method should:
   * 1. Check the state of the server, if the state of the server is already START, then return
   * 2. Loop through all the services, call service.start().
   */
  public void start() {
    logger.info("Start start()");
    if(ServerState.RUNNING.equals(getServerState())) return ;
    for(Service service : services.values()) {
      service.start() ; 
    }
    setServerState(ServerState.RUNNING) ;
    ClusterEvent clusterEvent = new ClusterEvent(ClusterEvent.ServerStateChange, getServerState()) ;
    clusterRPC.broadcast(clusterEvent);
    logger.info("Finish start()");
  }
  /**
   * This method is used to stop all the services usually it is used to simmulate the 
   * server shutdown or suspend.
   */
  public void shutdown() {
    logger.info("Start shutdown()");
    if(ServerState.SHUTDOWN.equals(getServerState())) return ;
    setServerState(ServerState.SHUTDOWN);
    ClusterEvent clusterEvent = new ClusterEvent(ClusterEvent.ServerStateChange, getServerState()) ;
    clusterRPC.broadcast(clusterEvent);
    logger.info("Finish shutdown()");
  }
  
  /**
   * This method is used to find a specifice service by the service id
   * @param serviceId
   * @return
   */
  public Service getService(String serviceId) {
    return services.get(serviceId) ;
  }
  
  /**
   * This method is used to find a specifice service by the service descriptor
   * @param descriptor
   * @return
   */
  public Service getService(ServiceDescriptor descriptor) {
    return getService(descriptor.getServiceId()) ;
  }
  
  public Service[] getServices() {
    return services.values().toArray(new Service[services.size()]) ;
  }
  
  /**
   * This method is used to dynamically add a service
   * @param service
   */
  public void register(Service service) {
    
  }
  
  /**
   * This method is used to dynamically remove a service
   * @param service
   */
  public void remove(String serviceId) {
    Service service = services.get(serviceId) ;
    if(service != null) {
      services.remove(serviceId) ;
      service.stop() ;
      service.onDestroy(null);
    }
  }
}