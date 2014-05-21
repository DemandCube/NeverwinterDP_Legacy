package com.neverwinterdp.server.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
  private ServerConfig server ;
  private Map<String, ServiceConfig> services = new LinkedHashMap<String, ServiceConfig>() ;
  private List<String> imports ;

  public ServerConfig getServer() { return server ; }
  public void setServer(ServerConfig server) {
    this.server = server ;
  }
  
  public ServiceConfig getServiceConfig(String id) {
    return services.get(id) ;
  }
  
  public List<ServiceConfig> getServices() {
    ArrayList<ServiceConfig> holder = new ArrayList<ServiceConfig>() ;
    holder.addAll(services.values()) ;
    return holder ;
  }
  
  public void setServices(List<ServiceConfig>  list) {
    for(ServiceConfig sel : list) {
      services.put(sel.getServiceId(), sel) ;
    }
  }
  
  public List<String> getImports() { return imports; }
  public void setImports(List<String> imports) {
    this.imports = imports;
  }
}