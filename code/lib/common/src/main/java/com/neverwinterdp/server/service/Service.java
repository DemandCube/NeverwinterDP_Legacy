package com.neverwinterdp.server.service;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 * This is a service or a service wrapper to another project such zookeeper , kafka, Vertx...
 */
public interface Service {
  /**
   * The service config contains the configuration information such service id, service version,
   * description, The real class implementation for this service interface. 
   * @return
   */
  public ServiceConfig     getServiceConfig() ;
  
  public void setServiceConfig(ServiceConfig config); 

  /**
   * The service descriptor contain the information of the service such name , version , the state
   * of the service so another service or remote service can decide to use this service or not. 
   * @return
   */
  public ServiceDescriptor getServiceDescriptor() ;
  
  /**
   * this method is called when the server init
   * @param server
   */
  public void onInit(Server server) ;
  
  /**
   * this method is called when the server destroy, the service should release all the resources
   * in this method.
   * @param server
   */
  public void onDestroy(Server server) ;
  
  /**
   * This method is designed to start the service and change the service state to START. 
   * If the service is a wrapper to another service such zookeeper, kafka... All the real service
   * state such load, config, init, start should be implemented in this method 
   */
  public void start() ;
  
  /**
   * This method is designed to stop the service and change the service state to STOP. 
   * If the service is a wrapper to another service such zookeeper, kafka... All the real service
   * state such stop, destroy should be implemented in this method 
   */
  public void stop() ;
}
