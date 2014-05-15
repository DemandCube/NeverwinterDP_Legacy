package com.neverwinterdp.server.command;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerDiscovery;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.service.Service;
import com.neverwinterdp.server.service.ServiceDescriptor;

public class ServerCommand {
  static public class Shutdown extends Command<ServerState> {
    public ServerState execute(Server server) throws Exception {
      server.shutdown() ; 
      return server.getClusterRPC().getMember().getState() ;
    }
  }
  
  static public class Start extends Command<ServerState> {
    public ServerState execute(Server server) throws Exception {
      server.start() ; 
      return server.getClusterRPC().getMember().getState() ;
    }
  }
  
  static public class GetServerDiscovery extends Command<ServerDiscovery> {
    public ServerDiscovery execute(Server server) throws Exception {
      ClusterMember member = server.getClusterRPC().getMember() ;
      Service[] services = server.getServices() ;
      ServiceDescriptor[] descriptors = new ServiceDescriptor[services.length] ;
      for(int i = 0; i < services.length; i++) {
        descriptors[i] = services[i].getServiceDescriptor() ;
      }
      return new ServerDiscovery(member, descriptors) ;
    }
  }
}
