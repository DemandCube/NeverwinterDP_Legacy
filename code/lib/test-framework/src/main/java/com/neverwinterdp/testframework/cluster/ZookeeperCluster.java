package com.neverwinterdp.testframework.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.util.FileUtil;

public class ZookeeperCluster implements ServiceCluster {
  final static public int    ZK_CLIENT_PORT = 2181 ;
  
  private List<ZookeeperServerInstance> servers ;
  
  private int port ;
  private int numberOfInstances ;
  private String baseDir ;
  
  public ZookeeperCluster(int port, int numberOfInstances) throws Exception {
    this.port = port ;
    this.numberOfInstances = numberOfInstances ;
  }
  
  public void init() throws Exception {
    servers = new ArrayList<ZookeeperServerInstance>() ;
    for(int i = 0; i < numberOfInstances; i++) {
      int id = i + 1 ;
      String serverName = "zookeeper-server" + id ;
      String dir = baseDir + "/" + serverName ;
      servers.add( new ZookeeperServerInstance(id, dir, port + i)) ;
    }
  }
  
  public String getName() { return "zookeeper" ; } 
  
  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir ;
  }
  
  public String[] getConnectURL() {
    List<String> holder = new ArrayList<String>() ;
    for(ZookeeperServerInstance server : servers) {
      holder.add(server.getConnectURL()) ;
    }
    return holder.toArray(new String[holder.size()]) ;
  }
  
  public String getConnectURLs() {
    return getConnectURLs(",") ;
  }
  
  public String getConnectURLs(String separator) {
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < servers.size(); i++) {
      ZookeeperServerInstance server = servers.get(i) ;
      if(i > 0) b.append(",") ;
      b.append(server.getConnectURL()) ;
    }
    return b.toString() ;
  }
  
  public void startup() throws Exception {
    for(ZookeeperServerInstance server : servers) {
      server.startup() ;
    }
  }
  
  public void shutdown() throws IOException {
    for(ZookeeperServerInstance server : servers) {
      server.shutdown() ;
    }
  }
  
  public void cleanup() throws Exception {
    FileUtil.removeIfExist(baseDir, true) ;
  }
}
