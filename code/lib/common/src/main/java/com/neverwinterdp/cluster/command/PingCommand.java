package com.neverwinterdp.cluster.command;


public class PingCommand extends Command<String> {
  private String message ;
  
  public PingCommand() {
    
  }
  
  public PingCommand(String message) {
    this.message = message ;
  }
  
  public String execute() throws Exception {
    return "Got your message '" + message + "'" ;
  }

}
