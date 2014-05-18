package com.neverwinterdp.server.cluster;

import java.io.Serializable;

import com.neverwinterdp.server.service.ServiceRegistration;

public class ClusterEvent implements Serializable {
  static public enum Type {
    ServerStateChange,
    ServiceStateChange
  }

  final static public Type ServerStateChange = Type.ServerStateChange ;
  final static public Type ServiceStateChange = Type.ServiceStateChange ;
  
  private Type              type;
  private ClusterMember     sourceMember ;
  private ServiceRegistration sourceService;
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

  public ServiceRegistration getSourceService() {
    return sourceService;
  }

  public void setSourceService(ServiceRegistration sourceService) {
    this.sourceService = sourceService;
  }

  public ClusterMember getSourceMember() {
    return sourceMember;
  }

  public void setSourceMember(ClusterMember sourceMember) {
    this.sourceMember = sourceMember ;
  }

  public Object getSource() {
    return source;
  }

  public void setSource(Object source) {
    this.source = source;
  }
  
  public String toString() {
    StringBuilder b = new StringBuilder() ;
    b.append("Event = " + type).
      append(", source member = ").append(sourceMember.toString()).
      append(", source  = ").append(source) ;
    return b.toString() ;
  }
}
