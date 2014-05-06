package com.neverwinterdp.cluster;

import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.sparkngin.HttpServer;

public class SparknginCluster {
  final static public int HTTP_PORT   = 8080;
  
  private List<HttpServer> servers ;
  
  public SparknginCluster(int port, int numberOfInstances) {
    servers = new ArrayList<HttpServer>() ;
    for(int i = 0; i < numberOfInstances; i++) {
      HttpServer server = new HttpServer(port + i);
      servers.add(server) ;
    }
  }
  
  public void startup() throws Exception {
    for(HttpServer server : servers) {
      server.start();
    }
  }
  
  public void shutdown() throws Exception {
    for(HttpServer server : servers) {
      server.stop();
    }
  }
}
