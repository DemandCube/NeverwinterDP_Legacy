package com.neverwinterdp.server.cluster;

import com.neverwinterdp.server.service.ServiceDescriptor;

public class ClusterEvent {
  static public enum Type {
    ServerStateChange,
    ServiceStateChange
  }

  private Type              type;
  private ClusterMember     sourceMember;
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

  public ClusterMember getSourceMember() {
    return sourceMember;
  }

  public void setSourceMember(ClusterMember sourceMember) {
    this.sourceMember = sourceMember;
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
