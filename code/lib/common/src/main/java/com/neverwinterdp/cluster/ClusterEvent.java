package com.neverwinterdp.cluster;

import com.neverwinterdp.cluster.service.ServiceDescriptor;

public class ClusterEvent {
  private String            name;
  private ClusterMember  sourceServer;
  private ServiceDescriptor sourceService;
  private Object            source ;
}
