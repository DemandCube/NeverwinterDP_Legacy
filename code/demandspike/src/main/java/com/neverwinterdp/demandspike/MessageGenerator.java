package com.neverwinterdp.demandspike;

import com.neverwinterdp.queuengin.Message;

public interface MessageGenerator<T> {
  public Message<T> next() ;
}
