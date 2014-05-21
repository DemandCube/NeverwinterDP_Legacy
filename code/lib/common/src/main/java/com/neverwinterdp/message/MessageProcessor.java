package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface MessageProcessor {
  public void onInit(MessageService service) ;
  public void onDestroy(MessageService service) ;
  
  void process(MessageService service, Message message) throws MessageException ;
}
