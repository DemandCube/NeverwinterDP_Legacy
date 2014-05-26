package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface MessageErrorHandler {
  public void onInit(MessageService service) ;
  public void onDestroy(MessageService service) ;
  
  void onError(MessageService service, Message message) ;
  void onReject(MessageService service, Message message) ;
  void onRetry(MessageService service, Message message) ;
  void onUnknown(MessageService service, Message message) ;
}
