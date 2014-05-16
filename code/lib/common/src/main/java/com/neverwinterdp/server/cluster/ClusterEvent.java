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
  private String            sourceMemberId ;
  private String            sourceAddress ;
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

  public String getSourceMemberId() {
    return sourceMemberId;
  }

  public void setSourceMemberId(String id) {
    this.sourceMemberId = id;
  }
  
  public String getSourceAddress() {
    return sourceAddress;
  }

  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  public void setSourceMember(ClusterMember sourceMember) {
    this.sourceMemberId = sourceMember.getId();
    this.sourceAddress = sourceMember.getIpAddress() + ":" + sourceMember.getPort() ;
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
      append(", source address = ").append(sourceAddress).
      append(", source  = ").append(source) ;
    return b.toString() ;
  }
}
