package com.neverwinterdp.message;

public interface MessageServicePlugin {
  String getName() ;
  void   setName(String name) ;
 
  void onPreProcess(MessageService service, Message<?> message) ;
  void onPostProcess(MessageService service, Message<?> message) ;

  void onErrorMessage(MessageService service, Message<?> message) ;
  void onRejectMessage(MessageService service, Message<?> message) ;
  void onRetryMessage(MessageService service, Message<?> message) ;
}
