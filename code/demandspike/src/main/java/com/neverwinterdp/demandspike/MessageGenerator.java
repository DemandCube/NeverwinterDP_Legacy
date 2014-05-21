package com.neverwinterdp.demandspike;

import com.neverwinterdp.message.Message;

public interface MessageGenerator<T> {
  public Message next() ;
}
