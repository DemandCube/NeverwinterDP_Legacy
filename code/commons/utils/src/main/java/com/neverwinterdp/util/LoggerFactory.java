package com.neverwinterdp.util;

import org.slf4j.Logger;

public class LoggerFactory {
  private String prefix ;
  
  public LoggerFactory() {
    this.prefix = "";
  }
  
  public LoggerFactory(String prefix) {
    this.prefix = prefix ;
  }
  
  public Logger getLogger(String name) {
    return org.slf4j.LoggerFactory.getLogger(prefix + name) ;
  }
  
  public Logger getLogger(Class<?> type) {
    return org.slf4j.LoggerFactory.getLogger(prefix + type.getName()) ;
  }
}
