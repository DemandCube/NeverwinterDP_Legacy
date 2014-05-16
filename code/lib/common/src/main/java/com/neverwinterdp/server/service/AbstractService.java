package com.neverwinterdp.server.service;

import org.slf4j.Logger;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;


abstract public class AbstractService implements Service {
  protected Logger logger ;
  
  private ServiceConfig     config ;
  private ServiceDescriptor descriptor = new ServiceDescriptor();
  
  public ServiceConfig getServiceConfig() {
    return config ;
  }
  
  public void setServiceConfig(ServiceConfig config) {
    this.config = config ;
    descriptor.init(config) ;
  }

  public ServiceDescriptor getServiceDescriptor() { return descriptor ; }
  
  public void onInit(Server server) {
    logger = server.getLogger(getClass().getSimpleName());
    logger.debug("onInit(Server server).......................");
  }
  
  public void onDestroy(Server server) {
  }
}
