package com.neverwinterdp.sparkngin;

import org.junit.After;
import org.junit.Before;

import com.neverwinterdp.queue.kafka.KafkaCluster;
import com.neverwinterdp.sparkngin.SparknginCluster;
import com.neverwinterdp.testframework.cluster.NeverwinterDPCluster;
import com.neverwinterdp.testframework.cluster.ZookeeperCluster;
import com.neverwinterdp.util.FileUtil;

abstract public class ClusterUnitTest {
  protected NeverwinterDPCluster cluster ;
  protected ZookeeperCluster zkCluster ;
  protected KafkaCluster kafkaCluster ;
  
  @Before
  public void setup() throws Exception {
    cluster = new NeverwinterDPCluster("target/cluster") ;
    cluster.cleanup();
    
    zkCluster = new ZookeeperCluster(ZookeeperCluster.ZK_CLIENT_PORT, 1);
    cluster.add(zkCluster) ;
    
    kafkaCluster = 
        new KafkaCluster(KafkaCluster.KAFKA_PORT, 2).
        setZookeeperConnectURL(zkCluster.getConnectURLs(",")) ;
    cluster.add(kafkaCluster);

    cluster.add(new SparknginCluster(SparknginCluster.HTTP_PORT, 2)) ;
    
    cluster.startup(); 
  }
  
  @After
  public void teardown() throws Exception {
    cluster.shutdown() ;
  }
}