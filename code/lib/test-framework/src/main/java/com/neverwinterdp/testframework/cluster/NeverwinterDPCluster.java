package com.neverwinterdp.testframework.cluster;

import java.util.ArrayList;
import java.util.List;

import com.neverwinterdp.util.FileUtil;

public class NeverwinterDPCluster implements ServiceCluster {
  private String baseDir ;
  private List<ServiceCluster> serviceClusters = new ArrayList<ServiceCluster>() ;
  
  public NeverwinterDPCluster(String baseDir) {
    this.baseDir = baseDir ;
  }
  
  public void init() throws Exception {
    
  }
  
  public String getName() { return "neverwinter" ; }
  
  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir ;
  }
  
  public NeverwinterDPCluster add(ServiceCluster sCluster) throws Exception {
    sCluster.setBaseDir(baseDir + "/" + sCluster.getName());
    sCluster.init(); 
    serviceClusters.add(sCluster) ;
    return this ;
  }
  
  public void startup() throws Exception {
    for(ServiceCluster sel : serviceClusters) {
      sel.startup(); 
    }
  }

  public void shutdown() throws Exception {
    for(ServiceCluster sel : serviceClusters) {
      sel.shutdown() ; 
    }
  }

  public void cleanup() throws Exception {
    for(ServiceCluster sel : serviceClusters) {
      sel.cleanup() ; 
    }
    FileUtil.removeIfExist(baseDir, true);
  }
  
}