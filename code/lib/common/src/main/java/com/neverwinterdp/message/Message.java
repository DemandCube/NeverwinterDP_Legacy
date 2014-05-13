package com.neverwinterdp.message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tuan Nguyen
 * @email tuan08@gmail.com
 */
public class Message {
  private MessageHeader            header = new MessageHeader();
  private MessageData              data;
  private List<MessageTrace>       traces;
  private List<MessageInstruction> instructions;

  public Message() {
  }

  public <T> Message(String key, T obj, boolean traceEnable) {
    header.setTraceEnable(traceEnable);
    ;
    header.setKey(key);
    data = new MessageData(null, obj);
  }

  public MessageHeader getHeader() {
    return this.header;
  }

  public void setHeader(MessageHeader header) {
    this.header = header;
  }

  public MessageData getData() {
    return this.data;
  }

  public void setData(MessageData data) {
    this.data = data;
  }

  public List<MessageTrace> getTraces() {
    return traces;
  }

  public void setTraces(List<MessageTrace> logs) {
    this.traces = logs;
  }

  public void addTrace(String serviceId, String message) {
    if (traces == null) traces = new ArrayList<MessageTrace>();
    traces.add(new MessageTrace(serviceId, message));
  }

  public List<MessageInstruction> getInstructions() {
    return instructions;
  }

  public void setInstructions(List<MessageInstruction> instructions) {
    this.instructions = instructions;
  }
}
