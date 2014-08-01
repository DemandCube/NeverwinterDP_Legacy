package com.neverwinterdp.ringbearer.job.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RingBearerJob implements Serializable {
  private String id ;
  private String description ;
  private String script ;
  private Map<String, Object> scriptProperties = new HashMap<String, Object>();
  
  private Map<String, Object> outputAttributes = new HashMap<String, Object>() ;
  
  public String getId() { return this.id ; }
  public void setId(String id) { this.id = id ; }
  
  public String getDescription() { return description; }
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getScript() { return this.script;}
  public void   setScript(String script) { this.script = script ;}
  
  public Map<String, Object> getScriptProperties() { return scriptProperties; }
  public void setScriptProperties(Map<String, Object> scriptProperties) {
    this.scriptProperties = scriptProperties;
  }
  
  public Map<String, Object> getOutputAttributes() {
    return outputAttributes;
  }
  
  public void setOutputAttributes(Map<String, Object> outputAttributes) {
    this.outputAttributes = outputAttributes;
  }
  
  public Object getOutputAttribute(String name) {
    return outputAttributes.get(name) ;
  }
  
  public void setOutputAttribute(String name, Object attribute) {
    outputAttributes.put(name, attribute) ;
  }
}