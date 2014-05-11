package com.neverwinterdp.sparkngin;

public class SendAck {
  static public enum Status {
    OK, ERROR, NOT_AVAIBLE
  }

  private Status status;
  private String message;

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
