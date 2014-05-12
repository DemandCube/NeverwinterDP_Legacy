package com.neverwinterdp.message;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.type.TypeReference;

import com.neverwinterdp.util.JSONSerializer;

public class Message<T> {
  private String       topic ;
  private String       key ;
  private byte[]       data;
  
  private boolean      logEnable ;
  private List<MessageLog>    logs;

  public Message() {
  }
  
  public Message(String key, T obj, boolean logEnable) {
    this.key = key ;
    setDataAs(obj) ;
    this.logEnable = logEnable ;
  }

  public String getTopic() { return this.topic ; }
  public void   setTopic(String topic) {
    this.topic = topic ;
  }
  
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  @JsonIgnore
  public T getDataAs(Class<T> type)  {
    if(data == null) return null ;
    return JSONSerializer.INSTANCE.fromBytes(data, type);
  }
  
  @JsonIgnore
  public List<T> getDataAs(TypeReference<List<T>> tref) throws Exception {
    return JSONSerializer.INSTANCE.fromBytes(data, tref);
  }
  
  @JsonIgnore
  public void setDataAs(T obj) {
    if(obj == null) {
      data = null; 
    } else {
      data = JSONSerializer.INSTANCE.toBytes(obj);
    }
  }

  
  public boolean isLogEnable() {
    return logEnable;
  }

  public void setLogEnable(boolean logEnable) {
    this.logEnable = logEnable;
  }

  public List<MessageLog> getLogs() {
    return logs;
  }

  public void setLogs(List<MessageLog> logs) {
    this.logs = logs;
  }
  
  public void addLog(String serviceId, String message) {
    if(logs == null) logs = new ArrayList<MessageLog>() ;
    logs.add(new MessageLog(serviceId, message)) ;
  }
}
