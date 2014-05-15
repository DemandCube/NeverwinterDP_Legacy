package com.neverwinterdp.server.service;

import java.io.Serializable;

import com.neverwinterdp.server.Server;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
abstract public class ServiceCommand<T> implements Serializable {
  private long timeout ;
  
  public long getTimeout() { return timeout; }
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  abstract public T execute(Server server, Service service) ;
}
