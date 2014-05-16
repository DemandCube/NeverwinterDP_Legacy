package com.neverwinterdp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.Service;
import com.neverwinterdp.server.service.ServiceDescriptor;
import com.neverwinterdp.server.service.ServiceState;

public class ServiceContainer {
  private Logger logger ;
  private Server server ;
  private Map<String, Service> services = new ConcurrentHashMap<String, Service>() ;
  
  
  public void onInit(Server server) {
    this.server = server ;
    this.logger = server.getLogger() ;
  }
  
  public void onDestroy(Server server) {
    for(Service service : services.values()) {
      service.onDestroy(server); ; 
    }
  }
  
  public void start() {
    for(Service service : services.values()) {
      service.start() ; 
      service.getServiceDescriptor().setState(ServiceState.START);
    }
  }
  
  public void stop() {
    for(Service service : services.values()) {
      service.stop() ; 
      service.getServiceDescriptor().setState(ServiceState.STOP);
    }
  }
  
  public List<ServiceDescriptor> getServiceDescriptors() {
    List<ServiceDescriptor> holder = new ArrayList<ServiceDescriptor>() ;
    for(Service service : services.values()) {
      holder.add(service.getServiceDescriptor()) ;
    }
    return holder ;
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
  
  public void register(ServiceConfig config) throws Exception {
    logger.info("Start register(ServiceConfig config), serviceId = " + config.getServiceId());
    String className = config.getClassName() ;
    Class<Service> clazz = (Class<Service>) Class.forName(className) ;
    Service instance = clazz.newInstance();
    instance.setServiceConfig(config) ;
    instance.onInit(server);
    if(!services.containsKey(config.getServiceId())) {
      services.put(config.getServiceId(), instance) ;
    } else {
      throw new Exception("Service " + config.getServiceId() + " is already registered") ;
    }
    logger.info("Finish register(ServiceConfig config), serviceId = " + config.getServiceId());
  }
  
  public void register(List<ServiceConfig> configs) throws Exception {
    if(configs == null) return ;
    for(ServiceConfig sel : configs) {
      register(sel) ;
    }
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