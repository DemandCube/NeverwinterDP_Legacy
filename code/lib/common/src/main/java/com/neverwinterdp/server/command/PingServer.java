package com.neverwinterdp.server.command;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.cluster.ClusterMember;


public class PingServer extends Command<String> {
  private String message ;
  
  public PingServer() { }
  
  public PingServer(String message) {
    this.message = message ;
  }
  
  public String execute(Server server) throws Exception {
    ClusterMember member = server.getClusterRPC().getMember() ;
    System.out.println("Execute by " + member.getIpAddress() + ":" + member.getPort()) ;
    return "Got your message '" + message + "'" ;
  }
}
