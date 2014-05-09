package com.neverwinterdp.message;

import org.codehaus.jackson.map.util.JSONPObject;

public interface MessageService {
  String getName() ;
  void   setName(String name);
  
  void onInit(JSONPObject config) ;
  void onDestroy() ;
  
  void process(Message<?> message) ;
}
