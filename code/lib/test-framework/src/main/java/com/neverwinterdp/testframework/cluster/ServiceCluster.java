package com.neverwinterdp.testframework.cluster;

public interface ServiceCluster {
  public String getName() ;
  public void init() throws Exception ;
  public void setBaseDir(String baseDir) ;
  public void startup() throws Exception ;
  public void shutdown() throws Exception ;
  public void cleanup() throws Exception ;
}