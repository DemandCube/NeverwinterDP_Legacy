package com.neverwinterdp.message;

import java.util.Map;

public class MessageInstruction {
  private String targetService ;
  private String instruction ;
  private Map<String, String> params ;
  
  public String getTargetService() { return targetService; }
  public void setTargetService(String targetService) {
    this.targetService = targetService;
  }
  
  public String getInstruction() { return instruction; }
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }
  
  public Map<String, String> getParams() { return params; }
  public void setParams(Map<String, String> properties) {
    this.params = properties;
  }
}
