package com.neverwinterdp.sparkngin;

import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.testframework.cluster.ServiceCluster;

public class SparknginCluster implements ServiceCluster {
  final static public int HTTP_PORT   = 8080;
  
  private List<HttpServer> servers ;
  int port;
  int numberOfInstances ;
  
  public SparknginCluster(int port, int numberOfInstances) {
    this.port = port ;
    this.numberOfInstances = numberOfInstances ;
  }

  public void init() throws Exception {
    servers = new ArrayList<HttpServer>() ;
    for(int i = 0; i < numberOfInstances; i++) {
      HttpServer server = new HttpServer(port + i);
      servers.add(server) ;
    }
  }
  
  public String getName() { return "sparkngin"; }

  public void setBaseDir(String baseDir) {
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

  public void cleanup() throws Exception {
  }
}
