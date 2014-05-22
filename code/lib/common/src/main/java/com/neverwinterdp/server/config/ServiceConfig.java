package com.neverwinterdp.server.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tuan Nguyen
 * @email tuan08@gmail.com
 */
public class ServiceConfig {
  private String              serviceId;
  private String              name;
  private float               version;
  private String              className;
  private String              description;
  
  private Map<String, Object> parameters = new HashMap<String, Object>();
  
  public String getServiceId() {
    if(serviceId == null) return className ;
    return this.serviceId;
  }

  public void setServiceId(String id) {
    this.serviceId = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getVersion() {
    return version;
  }

  public void setVersion(float version) {
    this.version = version;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
  
  public int getParameter(String name, int defaultValue) {
    Integer value = (Integer)parameters.get(name) ;
    if(value != null) return value.intValue() ;
    return defaultValue ;
  }
  
  public ServiceConfig setParameter(String name, int value) {
    parameters.put(name, value) ;
    return this ;
  }
  
  public String getParameter(String name, String defaultValue) {
    String value = (String)parameters.get(name) ;
    if(value != null) return value ;
    return defaultValue ;
  }
  
  public ServiceConfig setParameter(String name, String value) {
    parameters.put(name, value) ;
    return this ;
  }
  
  public <T> T getParameter(String name, T defaultValue) {
    T value = (T) parameters.get(name) ;
    if(value != null) return value ;
    return defaultValue ;
  }
  
  public <T> ServiceConfig setParameter(String name, T value) {
    parameters.put(name, value) ;
    return this ;
  }
}
