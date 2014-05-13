package com.neverwinterdp.server.cluster;

import java.util.List;

import com.neverwinterdp.server.ServerDescriptor;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandHandler;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class ClusterClient {
  private List<ServerDescriptor> serverDescriptors ;
  
  public List<ServerDescriptor> getServerDescriptors() {
    return serverDescriptors ;
  }
  
  public <T> T execute(ServerCommand<T> command) {
    return null ;
  }
  
  public <T> void execute(ServerCommand<T> command, ServerCommandHandler<T> handler) {
  }
}
