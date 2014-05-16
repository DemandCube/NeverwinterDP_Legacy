package com.neverwinterdp.server.cluster;

import java.io.Serializable;

import com.neverwinterdp.server.service.ServiceDescriptor;

public class ClusterEvent implements Serializable {
  static public enum Type {
    ServerStateChange,
    ServiceStateChange
  }

  final static public Type ServerStateChange = Type.ServerStateChange ;
  
  private Type              type;
  private ServiceDescriptor sourceService;
  private Object            source;

  public ClusterEvent() {

  }

  public ClusterEvent(Type type, Object source) {
    this.type = type;
    this.source = source;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type name) {
    this.type = name;
  }

  public ServiceDescriptor getSourceService() {
    return sourceService;
  }

  public void setSourceService(ServiceDescriptor sourceService) {
    this.sourceService = sourceService;
  }

  public Object getSource() {
    return source;
  }

  public void setSource(Object source) {
    this.source = source;
  }
}
