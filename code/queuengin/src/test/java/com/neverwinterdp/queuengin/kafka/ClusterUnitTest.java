package com.neverwinterdp.queuengin.kafka;

import org.junit.After;
import org.junit.Before;

import com.neverwinterdp.testframework.cluster.NeverwinterDPCluster;
import com.neverwinterdp.testframework.cluster.ZookeeperCluster;

abstract public class ClusterUnitTest {
  protected NeverwinterDPCluster cluster ;
  protected ZookeeperCluster zkCluster ;
  protected KafkaCluster kafkaCluster ;
  
  @Before
  public void setup() throws Exception {
    cluster = new NeverwinterDPCluster("build/cluster") ;
    cluster.cleanup();
    
    zkCluster = new ZookeeperCluster(ZookeeperCluster.ZK_CLIENT_PORT, 1);
    cluster.add(zkCluster) ;
    
    kafkaCluster = 
        new KafkaCluster(KafkaCluster.KAFKA_PORT, 2).
        setZookeeperConnectURL(zkCluster.getConnectURLs(",")) ;
    cluster.add(kafkaCluster);

    cluster.startup(); 
  }
  
  @After
  public void teardown() throws Exception {
    cluster.shutdown() ;
  }
}