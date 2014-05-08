package com.neverwinterdp.scribengin.dummy;

import java.util.HashMap;
import java.util.Map;
import com.neverwinterdp.queuengin.Message;

public class InMemoryMessageDB<T> {
  private Map<String, Message<T>> db = new HashMap<String, Message<T>>();
}
