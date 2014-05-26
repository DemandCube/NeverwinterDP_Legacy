package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public interface MessageServicePlugin {
  String getName() ;
 
  public void onInit() ;
  public void onDestroy() ;
  
  void onPreProcess(MessageService service, Message message) ;
  void onPostProcess(MessageService service, Message message) ;
}
