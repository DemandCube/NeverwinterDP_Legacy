package com.neverwinterdp.scribengin;

import com.neverwinterdp.queuengin.Message;

public interface MessageWriter {
  public void write(Message<?> message) ;
}
