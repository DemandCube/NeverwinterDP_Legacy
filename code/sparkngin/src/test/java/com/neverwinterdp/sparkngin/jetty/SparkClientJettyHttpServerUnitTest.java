package com.neverwinterdp.sparkngin.jetty;


import com.neverwinterdp.sparkngin.ClusterUnitTest;
import com.neverwinterdp.testframework.cluster.ServiceCluster;

public class SparkClientJettyHttpServerUnitTest extends ClusterUnitTest {
  protected ServiceCluster createSparknginServiceCluster() {
    return new SparknginJettyHttpServerCluster(SparknginJettyHttpServerCluster.HTTP_PORT, 2) ;
  }
}