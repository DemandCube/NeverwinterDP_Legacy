package com.neverwinterdp.message;

import java.util.List;

import com.neverwinterdp.message.MessageException.Type;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 * 
 *                         +-----------------+   +-----------------+   +-----------------+
 *                         | MessageServer   |   | MessageServer   |   | MessageServer   |
 *                         +-----------------+   +-----------------+   +-----------------+
 * +--------+              |                 |   |                 |   |                 |
 * | Client |---Message--->| MessageServices |-->| MessageServices |-->| MessageServices |
 * +--------+              |  (Sparkngin)    |   |   (Queuengin)   |   |  (Scribengin)   |
 *                         +-----------------+   +-----------------+   +-----------------+
 * MessageService is composed of several components that can be configured, reused or replaced by
 * the different implementation:
 * 1. MessageServiceDescriptor is the service configuration with the properties such service name,
 *    service id, service version, description, the topics that the service are listen to.
 * 2. MessageProcessor: the logic to process the message such forward to sink, 
 *    route to another topic...
 * 3. MessageServicePlugin is designed to reuse certain code and logic such add trace log to the 
 *    message, monitor... The plugin is called before and after the message is processed with 2 
 *    methods onPreProcess(Message) and onPostProcess(Message)
 * 3. MessageErrorHandler is designed to handle the exception when the MessageProcessor throw the 
 *    exceptions such Rejected, Error, Retry, Unknown. 
 */
public class MessageService {
  private MessageServiceDescriptor descriptor ;
  private MessageProcessor processor ;
  private List<MessageServicePlugin> messagePlugins ;
  private List<MessageErrorHandler>  errorHandlers ;
  
  public MessageServiceDescriptor getDescriptor() { return descriptor ; }
  public void setDescriptor(MessageServiceDescriptor descriptor) {
    this.descriptor = descriptor ;
  }
  
  public void onInit() {
    for(MessageServicePlugin plugin : messagePlugins) {
      plugin.onInit() ;
    }
  }
  
  public void onDestroy() {
    for(MessageServicePlugin plugin : messagePlugins) {
      plugin.onDestroy() ;
    }
  }
  
  void process(Message message) {
    try { 
      for(MessageServicePlugin plugin : messagePlugins) {
        plugin.onPreProcess(this, message);
      }
      processor.process(this, message) ;
      for(MessageServicePlugin plugin : messagePlugins) {
        plugin.onPostProcess(this, message);
      }
    } catch(MessageException ex) {
      Type type = ex.getType() ;
      if(Type.REJECTED.equals(type)) {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onReject(this, message);
        }
      } else if(Type.ERROR.equals(type)) {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onError(this, message);
        }
      } else {
        for(MessageErrorHandler handler : errorHandlers) {
          handler.onUnknown(this, message);
        }
      }
    }
  }
}
