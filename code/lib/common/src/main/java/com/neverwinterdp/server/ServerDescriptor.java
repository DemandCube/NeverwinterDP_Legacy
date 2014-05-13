package com.neverwinterdp.server;

import java.util.List;

import com.neverwinterdp.service.ServiceDescriptor;

public class ServerDescriptor {
  private String                  name;
  private String                  group;
  private String                  host;
  private float                   version;
  private int                     listenPort;
  private String                  description;
  private ServerState             state ;
  private List<ServiceDescriptor> serviceDescriptors ;
  
}
