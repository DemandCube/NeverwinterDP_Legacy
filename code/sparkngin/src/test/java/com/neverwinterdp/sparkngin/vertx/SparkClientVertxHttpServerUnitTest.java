package com.neverwinterdp.sparkngin.vertx;


import com.neverwinterdp.sparkngin.ClusterUnitTest;
import com.neverwinterdp.testframework.cluster.ServiceCluster;

public class SparkClientVertxHttpServerUnitTest extends ClusterUnitTest {
  protected ServiceCluster createSparknginServiceCluster() {
    return new SparknginVertxHttpServerCluster(SparknginVertxHttpServerCluster.HTTP_PORT, 2) ;
  }
}