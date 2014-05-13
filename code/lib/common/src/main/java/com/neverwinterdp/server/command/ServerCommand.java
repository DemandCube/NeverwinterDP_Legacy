package com.neverwinterdp.server.command;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerDescriptor;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface ServerCommand<T> {
  public ServerDescriptor[] getTargetServer() ;
  public T execute(Server server) ;
}
