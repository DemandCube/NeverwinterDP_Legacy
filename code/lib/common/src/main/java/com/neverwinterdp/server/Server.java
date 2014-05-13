package com.neverwinterdp.server;

import java.util.List;

import com.neverwinterdp.server.cluster.ClusterService;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.service.Service;
import com.neverwinterdp.service.ServiceCommand;
import com.neverwinterdp.service.ServiceDescriptor;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * The server can be understood as a single container or a machine that contains an unlimited number
 * of the services.
 */
public class Server {
  /**
   * The configuration for the server such name, group, version, description, listen port. It also
   * may contain the service configurations
   * */
  private ServerConfig serverConfig ;
  /**
   * The server descriptor contain the information such hostname, listen port, the state of the server,
   * and the available services that the server provide with the service running status. 
   */
  private ServerDescriptor descriptor ;
  
  public ServerConfig getServerConfig() { return this.serverConfig ; }
  
  public void  setServerConfig(ServerConfig config) { this.serverConfig = config ; }
  
  public ServerDescriptor getServerDescriptor() { return descriptor ; }
  
  /**
   * This lifecycle method be called after the configuration is set. The method should:
   * 1. Compute the configuration and add the services with the service configuration.
   * 2. Loop through the services and call service.onInit()
   * 3. Set the state of the services to init.
   * 4. Set the state of the server to init. 
   * 5. Add and configure the cluster services, start the cluster services
   */
  public void onInit() {
    
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
  public void startServices() {
    
  }
  
  /**
   * This method is used to stop all the services usually it is used to simmulate the 
   * server shutdown or suspend.
   */
  public void stopServices() {
  
  }
  
  /**
   * This method is called after the stopServices is called. This method should:
   * 1. Stop and destroy all the cluster services
   * 2. Release all the resources if necessary, save the monitor or profile information.
   */
  public void onDestroy() {
    
  }
  
  /**
   * This method is used to get a ClusterService. All the cluster service should be configured,
   * initialized and start in the onInit(). All the cluster service should live with the server
   * until the server instance is destroyed.
   * @param serviceId
   * @return
   */
  public ClusterService getClusterService(String serviceId) {
    return null ;
  }
  
  /**
   * This method is used to find a specifice service by the service id
   * @param serviceId
   * @return
   */
  public Service getService(String serviceId) {
    return null ;
  }
  /**
   * This method is used to find a specifice service by the service descriptor
   * @param descriptor
   * @return
   */
  public Service getService(ServiceDescriptor descriptor) {
    return getService(descriptor.getServiceId()) ;
  }
  
  public List<Service> getServices() {
    return null ;
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
    
  }
  
  

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
  public <T> T execute(ServiceCommand<T> command) {
    return null ;
  }
  
  /**
   * This method is designed to be called by the cluster service only. When the server is in the 
   * SHUTDOWN state, the cluster service is still functioned and listen to the cluster command
   * @param command
   * @return
   */
  public <T> T execute(ServerCommand<T> command) {
    return null ;
  }
}