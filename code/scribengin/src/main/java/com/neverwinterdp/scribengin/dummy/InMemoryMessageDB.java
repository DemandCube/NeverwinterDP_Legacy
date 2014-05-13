package com.neverwinterdp.scribengin.dummy;

import java.util.HashMap;
import java.util.Map;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.scribengin.MessageWriter;

public class InMemoryMessageDB implements MessageWriter {
  private Map<String, Message> db = new HashMap<String, Message>();

  public void write(Message message) {
    db.put(message.getHeader().getKey(), message) ;
  }
  
  public int count() { return db.size() ; }
}
