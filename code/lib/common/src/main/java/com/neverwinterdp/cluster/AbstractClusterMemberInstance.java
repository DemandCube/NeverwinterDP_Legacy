package com.neverwinterdp.cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.neverwinterdp.cluster.command.Command;
import com.neverwinterdp.cluster.service.Service;
import com.neverwinterdp.cluster.service.ServiceCommand;
import com.neverwinterdp.cluster.service.ServiceDescriptor;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
abstract public class AbstractClusterMemberInstance implements ClusterMemberInstance {
  private ClusterMemberConfig serverConfig ;
  private ClusterMember member ;
  private Map<String, Service> services = new ConcurrentHashMap<String, Service>() ;  
  
  public ClusterMemberConfig getConfig() { return this.serverConfig ; }
  
  public void  setConfig(ClusterMemberConfig config) { this.serverConfig = config ; }
  
  public ClusterMember getMember() { return member ; }
  public void setMember(ClusterMember member) { this.member = member ;}
  
  public void onInit() {
    
  }
  
  public void startServices() {
    for(Service service : services.values()) {
      service.start(); 
    }
  }
  
  public void stopServices() {
    for(Service service : services.values()) {
      service.stop() ; 
    }
  }
  
  public void onDestroy() {
    
  }
  
  
  public Service getService(String serviceId) { return services.get(serviceId) ; }

  public Service getService(ServiceDescriptor descriptor) {
    return getService(descriptor.getServiceId()) ;
  }
  
  public Service[] getServices() {
    return services.values().toArray(new Service[services.size()]) ;
  }
  
  public void register(Service service) {
    
  }
  
  public void remove(String serviceId) { 
    Service service = services.get(serviceId) ;
    if(service != null) {
      services.remove(serviceId) ;
      service.stop() ;
      service.onDestroy(this);
    }
  }
}