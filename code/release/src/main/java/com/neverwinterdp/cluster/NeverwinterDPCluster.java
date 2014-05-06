package com.neverwinterdp.cluster;

public class NeverwinterDPCluster {
  static String HOST_NAME = "Tuan" ;
  
  private String baseDir ;
  private int numberOfZKServerInstances = 1;
  private int numberOfKafkaServerInstances = 1;
  private int numberOfHttpServerInstances = 1;
  
  private ZookeeperCluster zkCluster ;
  private KafkaCluster kafkaCluster ;
  private SparknginCluster httpCluster ;
  
  public NeverwinterDPCluster(String baseDir) {
    this.baseDir = baseDir ;
  }

  public NeverwinterDPCluster setNumberOfZKServerInstances(int number) {
    numberOfZKServerInstances = number ;
    return this ;
  }
  
  public NeverwinterDPCluster setNumberOfKafkaServerInstances(int number) {
    numberOfKafkaServerInstances = number ;
    return this ;
  }
  
  public NeverwinterDPCluster setNumberOfHttpServerInstances(int number) {
    numberOfHttpServerInstances = number ;
    return this ;
  }
  
  public ZookeeperCluster getZookeeperCluster() { return zkCluster ; }
  
  public KafkaCluster getKafkaCluster() { return this.kafkaCluster ; }
  
  public void startup() throws Exception {
    String zkDir = baseDir + "/zookeeper";
    zkCluster = new ZookeeperCluster(zkDir, ZookeeperCluster.ZK_CLIENT_PORT, numberOfZKServerInstances);
    zkCluster.startup() ;
    
    String kafkaBaseDir = baseDir + "/kafka";
    kafkaCluster = new KafkaCluster(kafkaBaseDir, KafkaCluster.KAFKA_PORT, numberOfKafkaServerInstances);
    kafkaCluster.
      setZookeeperConnectURL(zkCluster.getConnectURLs(",")).
      startup();
    
    httpCluster = new SparknginCluster(SparknginCluster.HTTP_PORT, numberOfHttpServerInstances);
    httpCluster.startup() ;
  }
  
  public void shutdown() throws Exception {
    httpCluster.shutdown() ;
    kafkaCluster.shutdown() ;
    zkCluster.shutdown() ;
  }
  
  public void cleanup() throws Exception {
    kafkaCluster.cleanup() ;
    zkCluster.cleanup() ;
  }
}