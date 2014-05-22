package com.neverwinterdp.sparkngin.vertx.impl;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.json.JsonObject;

import com.neverwinterdp.testframework.cluster.ServiceCluster;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class SparknginVertxHttpServerCluster implements ServiceCluster {
  final static public int HTTP_PORT   = 8080;
  
  private List<EmbbededVertxServer> servers ;
  int port;
  int numberOfInstances ;
  
  public SparknginVertxHttpServerCluster(int port, int numberOfInstances) {
    this.port = port ;
    this.numberOfInstances = numberOfInstances ;
  }

  public void init() throws Exception {
    servers = new ArrayList<EmbbededVertxServer>() ;
    for(int i = 0; i < numberOfInstances; i++) {
      EmbbededVertxServer server = new EmbbededVertxServer();
      servers.add(server) ;
    }
  }
  
  public String getName() { return "sparkngin"; }

  public void setBaseDir(String baseDir) {
  }
  
  public void startup() throws Exception {
    for(int i = 0; i < servers.size(); i++) {
      JsonObject config = new JsonObject() ;
      EmbbededVertxServer server = servers.get(i) ;
      config.putNumber("http-listen-port", new Integer(port + i)) ;
      server.deployVerticle(HttpServerVerticle.class, config, 1);
    }
    Thread.sleep(3000);
    System.out.println("cluster startup done!!!") ;
  }
  
  public void shutdown() throws Exception {
    for(EmbbededVertxServer server : servers) {
      server.stop();
    }
  }

  public void cleanup() throws Exception {
  }
}
