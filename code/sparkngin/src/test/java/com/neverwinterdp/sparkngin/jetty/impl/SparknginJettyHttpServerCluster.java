package com.neverwinterdp.sparkngin.jetty.impl;

import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.testframework.cluster.ServiceCluster;

public class SparknginJettyHttpServerCluster implements ServiceCluster {
  final static public int HTTP_PORT   = 8080;
  
  private List<JettyHttpServer> servers ;
  int port;
  int numberOfInstances ;
  
  public SparknginJettyHttpServerCluster(int port, int numberOfInstances) {
    this.port = port ;
    this.numberOfInstances = numberOfInstances ;
  }

  public void init() throws Exception {
    servers = new ArrayList<JettyHttpServer>() ;
    for(int i = 0; i < numberOfInstances; i++) {
      JettyHttpServer server = new JettyHttpServer(port + i);
      servers.add(server) ;
    }
  }
  
  public String getName() { return "sparkngin"; }

  public void setBaseDir(String baseDir) {
  }
  
  public void startup() throws Exception {
    for(JettyHttpServer server : servers) {
      server.start();
    }
  }
  
  public void shutdown() throws Exception {
    for(JettyHttpServer server : servers) {
      server.stop();
    }
  }

  public void cleanup() throws Exception {
  }
}
