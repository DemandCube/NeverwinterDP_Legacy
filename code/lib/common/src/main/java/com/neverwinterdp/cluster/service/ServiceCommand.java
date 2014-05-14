package com.neverwinterdp.cluster.service;

import com.neverwinterdp.cluster.ClusterMemberInstance;
import com.neverwinterdp.cluster.ClusterMember;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface ServiceCommand<T> {
  public ClusterMember[] getTargetServer() ;
  public ServiceDescriptor getTargetService() ;
  
  public T execute(ClusterMemberInstance server, Service service) ;
}
