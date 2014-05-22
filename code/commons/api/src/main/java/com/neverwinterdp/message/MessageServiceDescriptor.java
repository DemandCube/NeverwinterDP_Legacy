package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class MessageServiceDescriptor {
  private String   name;
  private float    version;
  private String[] listenTopic;
  private String   description;

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

  public String[] getListenTopic() {
    return listenTopic;
  }

  public void setListenTopic(String[] listenTopic) {
    this.listenTopic = listenTopic;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
