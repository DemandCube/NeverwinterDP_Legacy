package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.ServerDescriptor;
import com.neverwinterdp.service.ServiceDescriptor;

public class ClusterEvent {
  private String            name;
  private ServerDescriptor  sourceServer;
  private ServiceDescriptor sourceService;
  private Object            source ;
}
