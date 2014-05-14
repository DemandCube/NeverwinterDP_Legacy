package com.neverwinterdp.cluster;

/**
 * @author Tuan Nguyen
 * @email tuan08@gmail.com
 */
public class ClusterMemberConfig {
  private String name;
  private String group;
  private String host;
  private float  version;
  private int    listenPort;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
