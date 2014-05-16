package com.neverwinterdp.server.command;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerDiscovery;
import com.neverwinterdp.server.ServerState;

public class ServerCommand {
  static public class Ping extends Command<ServerState> {
    public ServerState execute(Server server) throws Exception {
      return server.getServerState() ;
    }
  }

  
  static public class Shutdown extends Command<ServerState> {
    public ServerState execute(Server server) throws Exception {
      server.shutdown() ; 
      return server.getServerState() ;
    }
  }
  
  static public class Start extends Command<ServerState> {
    public ServerState execute(Server server) throws Exception {
      server.start() ; 
      return server.getServerState() ;
    }
  }
  
  static public class GetServerDiscovery extends Command<ServerDiscovery> {
    public ServerDiscovery execute(Server server) throws Exception {
      return server.getServerDiscovery() ;
    }
  }
}
