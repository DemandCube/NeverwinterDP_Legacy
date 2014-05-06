package com.neverwinterdp.queue.kafka;

import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.MockTime;

public class KafkaServerInstance {
  private int                id ;
  private String             logDir;
  private int                listenPort;
  private String             zookeeperConnectURL ;
  private KafkaServer        server;

  public KafkaServerInstance(int id, String logDir, int listenPort) throws Exception {
    this.id = id ;
    this.logDir = logDir ;
    this.listenPort = listenPort ;
  }

  public KafkaServerInstance setZookeeperConnectURL(String url) {
    this.zookeeperConnectURL = url ;
    return this ;
  }
  
  public String getConnectionURL() {
    return "127.0.0.1:" + listenPort ;
  }
  
  public void startup() {
    if(server != null) {
      throw new RuntimeException("Kafka server is already launched!") ;
    }
    Properties props = new Properties();
    props.setProperty("hostname", "127.0.0.1");
    props.setProperty("port", Integer.toString(listenPort));
    props.setProperty("broker.id", Integer.toString(id));
    props.setProperty("auto.create.topics.enable", "true");
    props.setProperty("log.dir", logDir);
    props.setProperty("enable.zookeeper", "true");
    props.setProperty("zookeeper.connect", zookeeperConnectURL);
    server = new KafkaServer(new KafkaConfig(props), new MockTime());
    server.startup();
    System.out.println("Launched kafka server " + id);
  }
  
  public void shutdown() {
    if(server == null) return ;
    server.shutdown() ;
    System.out.println("Shutdown Kafka Server " + id);
  }
}