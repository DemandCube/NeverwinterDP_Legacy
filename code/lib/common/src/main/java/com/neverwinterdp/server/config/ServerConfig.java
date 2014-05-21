package com.neverwinterdp.server.config;

/**
 * @author Tuan Nguyen
 * @email tuan08@gmail.com
 */
public class ServerConfig {
  private String   group;
  private String   host;
  private int      listenPort;
  private float    version;
  private String[] roles;
  private String   clusterRPC;
  private String   description;

  public String getGroup() { return group; }
  public void setGroup(String clusterName) {
    this.group = clusterName;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public float getVersion() {
    return version;
  }

  public void setVersion(float version) {
    this.version = version;
  }

  public int getListenPort() {
    return listenPort;
  }

  public void setListenPort(int listenPort) {
    this.listenPort = listenPort;
  }

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  public String getClusterRPC() {
    return clusterRPC;
  }

  public void setClusterRPC(String clusterRPC) {
    this.clusterRPC = clusterRPC;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
