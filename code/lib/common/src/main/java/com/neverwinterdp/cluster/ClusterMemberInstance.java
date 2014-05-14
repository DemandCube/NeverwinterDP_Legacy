package com.neverwinterdp.cluster;

import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.command.Result;
import com.neverwinterdp.cluster.service.Service;
import com.neverwinterdp.cluster.service.ServiceCommand;
import com.neverwinterdp.cluster.service.ServiceDescriptor;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * The server can be understood as a single container or a machine that contains an unlimited number
 * of the services.
 */
public interface ClusterMemberInstance {
  
  /**
   * The configuration for the server such name, group, version, description, listen port. It also
   * may contain the service configurations
   * */
  public ClusterMemberConfig getConfig() ;
  
  /**
   * The server descriptor contain the information such hostname, listen port, the state of the server,
   * and the available services that the server provide with the service running status. 
   */
  public ClusterMember getMember() ;
  
  /**
   * This lifecycle method be called after the configuration is set. The method should:
   * 1. Compute the configuration and add the services with the service configuration.
   * 2. Loop through the services and call service.onInit()
   * 3. Set the state of the services to init.
   * 4. Set the state of the server to init. 
   * 5. Add and configure the cluster services, start the cluster services
   */
  public void onInit() ;
  
  /**
   * This method lifecycle should be called after the method onInit() is called. 
   * This method should be called by the external service that can access the Server instance 
   * or the cluster services.
   * 
   * This method should:
   * 1. Check the state of the server, if the state of the server is already START, then return
   * 2. Loop through all the services, call service.start().
   */
  public void startServices() ;
  
  /**
   * This method is used to stop all the services usually it is used to simmulate the 
   * server shutdown or suspend.
   */
  public void stopServices() ;
  
  /**
   * This method is called after the stopServices is called. This method should:
   * 1. Stop and destroy all the cluster services
   * 2. Release all the resources if necessary, save the monitor or profile information.
   */
  public void onDestroy() ;
  
  /**
   * This method is used to find a specifice service by the service id
   * @param serviceId
   * @return
   */
  public Service getService(String serviceId) ;
  
  /**
   * This method is used to find a specifice service by the service descriptor
   * @param descriptor
   * @return
   */
  public Service getService(ServiceDescriptor descriptor) ;
  
  public Service[] getServices() ;
  
  /**
   * This method is used to dynamically add a service
   * @param service
   */
  public void register(Service service) ;
  
  /**
   * This method is used to dynamically remove a service
   * @param service
   */
  public void remove(String serviceId) ;
  

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
  public <T> T execute(ServiceCommand<T> command) ;
  
  /**
   * This method is designed to be called by the cluster only. When the server is in the 
   * SHUTDOWN state, the cluster service is still functioned and listen to the cluster command
   * @param command
   * @return
   */
  public <T> Result<T> execute(Command<T> command, ClusterMember member) ;
  
  public <T> Result<T>[] execute(Command<T> command, ClusterMember[] member) ;
}