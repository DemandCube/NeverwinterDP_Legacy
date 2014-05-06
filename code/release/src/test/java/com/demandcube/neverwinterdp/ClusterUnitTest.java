package com.demandcube.neverwinterdp;

import org.junit.After;
import org.junit.Before;

import com.neverwinterdp.cluster.NeverwinterDPCluster;
import com.neverwinterdp.cluster.ZookeeperCluster;
import com.neverwinterdp.util.FileUtil;

abstract public class ClusterUnitTest {
  protected NeverwinterDPCluster cluster ;
  protected ZookeeperCluster zkCluster ;
  
  @Before
  public void setup() throws Exception {
    FileUtil.removeIfExist("target/cluster", false) ;
    cluster = new NeverwinterDPCluster("target/cluster") ;
    cluster.
      setNumberOfZKServerInstances(1). //Bug zookeeper, can start only one instance
      setNumberOfKafkaServerInstances(2).
      setNumberOfHttpServerInstances(2).
      startup() ;
    zkCluster = cluster.getZookeeperCluster();
  }
  
  @After
  public void teardown() throws Exception {
    cluster.shutdown() ;
  }
}