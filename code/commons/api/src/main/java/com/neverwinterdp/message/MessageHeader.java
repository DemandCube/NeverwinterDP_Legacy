package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email tuan08@gmail.com
 */
public class MessageHeader {
  private float      version;
  private String     topic;
  private String     key;
  private boolean    traceEnable;
  private boolean    instructionEnable;

  public float getVersion() {
    return version;
  }

  public void setVersion(float version) {
    this.version = version;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public boolean isTraceEnable() {
    return traceEnable;
  }

  public void setTraceEnable(boolean b) {
    this.traceEnable = b;
  }

  public boolean isInstructionEnable() {
    return instructionEnable;
  }

  public void setInstructionEnable(boolean instructionEnable) {
    this.instructionEnable = instructionEnable;
  }
}