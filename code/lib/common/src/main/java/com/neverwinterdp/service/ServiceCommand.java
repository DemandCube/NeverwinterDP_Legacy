package com.neverwinterdp.service;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerDescriptor;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface ServiceCommand<T> {
  public ServerDescriptor[] getTargetServer() ;
  public ServiceDescriptor getTargetService() ;
  
  public T execute(Server server, Service service) ;
}
