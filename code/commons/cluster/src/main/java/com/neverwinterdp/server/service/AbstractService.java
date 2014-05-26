package com.neverwinterdp.server.service;

import org.slf4j.Logger;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;


abstract public class AbstractService implements Service {
  protected Logger logger ;
  
  private ServiceConfig     config ;
  private ServiceRegistration registration = new ServiceRegistration();
  
  public ServiceConfig getServiceConfig() {
    return config ;
  }
  
  public void setServiceConfig(ServiceConfig config) {
    this.config = config ;
    registration.init(config) ;
  }

  public ServiceRegistration getServiceRegistration() { return registration ; }
  
  public void onInit(Server server) {
    logger = server.getLoggerFactory().getLogger(getClass().getSimpleName());
    logger.debug("onInit(Server server).......................");
  }
  
  public void onDestroy(Server server) {
  }
}
