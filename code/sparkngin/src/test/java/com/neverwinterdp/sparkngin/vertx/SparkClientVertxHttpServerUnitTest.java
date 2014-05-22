package com.neverwinterdp.sparkngin.vertx;


import com.neverwinterdp.sparkngin.ClusterUnitTest;
import com.neverwinterdp.sparkngin.vertx.impl.SparknginVertxHttpServerCluster;
import com.neverwinterdp.testframework.cluster.ServiceCluster;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class SparkClientVertxHttpServerUnitTest extends ClusterUnitTest {
  protected ServiceCluster createSparknginServiceCluster() {
    return new SparknginVertxHttpServerCluster(SparknginVertxHttpServerCluster.HTTP_PORT, 2) ;
  }
}