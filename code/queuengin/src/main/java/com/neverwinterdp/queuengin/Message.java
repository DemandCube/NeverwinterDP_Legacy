package com.neverwinterdp.queuengin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.type.TypeReference;

import com.neverwinterdp.util.JSONSerializer;

public class Message<T> {
  private String       key ;
  private byte[]       data;
  
  private boolean      logEnable ;
  private List<Log>    logs;

  public Message() {
  }
  
  public Message(String key, T obj, boolean logEnable) throws IOException {
    this.key = key ;
    setDataAs(obj) ;
    this.logEnable = logEnable ;
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
  public T getDataAs(Class<T> type) throws Exception {
    if(data == null) return null ;
    return JSONSerializer.INSTANCE.fromBytes(data, type);
  }
  
  @JsonIgnore
  public List<T> getDataAs(TypeReference<List<T>> tref) throws Exception {
    return JSONSerializer.INSTANCE.fromBytes(data, tref);
  }
  
  @JsonIgnore
  public void setDataAs(T obj) throws IOException {
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

  public List<Log> getLogs() {
    return logs;
  }

  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }
  
  public void addLog(String serviceId, String message) {
    if(logs == null) logs = new ArrayList<Log>() ;
    logs.add(new Log(serviceId, message)) ;
  }
  
  static public class Log {
    final static public SimpleDateFormat COMPACT_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss")  ;
    private String serviceId ;
    private long   time ;
    private String message ;
    
    public Log() {} 
    
    public Log(String serviceId, String message) {
      this.serviceId = serviceId ;
      this.time = System.currentTimeMillis() ;
      this.message = message ;
    }
    
    public String getServiceId() {
      return serviceId;
    }

    public void setServiceId(String serviceId) {
      this.serviceId = serviceId;
    }

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
    
    public String toString() {
      StringBuilder b = new StringBuilder("  ") ;
      b.append(serviceId).
        append("[").
        append(COMPACT_DATE_TIME.format(new Date(time))).
        append("] ").
        append(message) ;
      return b.toString() ;
    }
  }
}
