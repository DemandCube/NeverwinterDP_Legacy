package com.neverwinterdp.queuengin.kafka;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.testframework.cluster.ServiceCluster;
import com.neverwinterdp.util.FileUtil;


public class KafkaCluster implements ServiceCluster {
  final static public String KAFKA_LOG_DIR       = "target" + File.separator + "kafka";
  final static public int    KAFKA_PORT   = 9090;
  
  private String baseDir ;
  int port;
  int numOfInstances ;
  String zookeeperConnectUrls ;
  private List<KafkaServerInstance> servers ;
  
  public KafkaCluster(int port, int numOfInstances) throws Exception {
    this.port = port ;
    this.numOfInstances = numOfInstances; 
  }
  
  public void init() throws Exception {
    servers = new ArrayList<KafkaServerInstance>() ;
    for(int i = 0; i < numOfInstances; i++) {
      int id = i + 1; 
      String serverName = "kserver-" + id ;
      String logDir = baseDir + "/" + serverName ;
      logDir = logDir.replace("/", File.separator) ;
      KafkaServerInstance server = new KafkaServerInstance(id, logDir, (port + i)) ;
      server.setZookeeperConnectURL(this.zookeeperConnectUrls);
      servers.add(server);
    }
  }
  
  public String getName() { return "kafka" ; }
  
  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir ;
  }
  
  public String[] getConnectionURL() {
    List<String> holder = new ArrayList<String>() ;
    for(KafkaServerInstance sel : servers) {
      holder.add(sel.getConnectionURL()) ;
    }
    return holder.toArray(new String[holder.size()]) ;
  }
  
  public String getConnectionURLs() {
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < servers.size(); i++) {
      KafkaServerInstance sel = servers.get(i) ;
      if(i > 0) b.append(",") ;
      b.append(sel.getConnectionURL()) ;
    }
    return b.toString() ;
  }
  
  public KafkaCluster setZookeeperConnectURL(String url) {
    this.zookeeperConnectUrls = url ;
    return this ;
  }
  
  public void startup() {
    for(KafkaServerInstance server : servers) {
      server.startup();
    }
  }
  
  public void shutdown() {
    for(KafkaServerInstance server : servers) {
      server.shutdown();
    }
  }
  
  public void cleanup() throws Exception {
    FileUtil.removeIfExist(baseDir, false) ;
  }
}
