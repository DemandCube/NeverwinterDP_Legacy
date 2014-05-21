package com.neverwinterdp.message;

/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class MessageException extends Exception {
  static public enum Type { REJECTED, ERROR, UNKNOWN }
  
  private Type type ;
  
  /**
   * Use to create an empty exception. This constructor should be used only in the 
   * serialization and deserializing
   */
  public MessageException() {
    
  }
  
  public MessageException(Type type, String message) {
    super(message) ;
    this.type = type ;
  }
  
  public MessageException(Type type, String message, Throwable cause) {
    super(message, cause) ;
    this.type = type ;
  }
  
  public Type getType() { return this.type ; }
}
