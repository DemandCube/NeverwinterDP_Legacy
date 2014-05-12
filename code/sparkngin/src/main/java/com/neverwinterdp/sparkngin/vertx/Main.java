package com.neverwinterdp.sparkngin.vertx;

import org.vertx.java.core.json.JsonObject;

public class Main {
  static String getValue(String arg) {
    String[] array = arg.split("=", 2) ;
    return array[1] ;
  }
  
  static public void main(String[] args) throws Exception {
    String brokerList = "127.0.0.1:9092" ;
    int listenPort = 8080 ;

    System.out.println("Available options: ");
    System.out.println("  --listen-port=8080");
    System.out.println("  --broker-list=127.0.0.1:9092");
    if(args != null) {
      for(String arg : args) {
        if(arg.startsWith("--listen-port")) listenPort = Integer.parseInt(getValue(arg)) ;
        else if(arg.startsWith("--broker-list")) brokerList = getValue(arg) ;
        else {
          System.out.println("Unknown option: " + arg);
          return ;
        }
      }
    }
    
    EmbbededVertxServer server = new EmbbededVertxServer() ; 
    JsonObject config = new JsonObject() ;
    config.putNumber("http-listen-port", new Integer(listenPort)) ;
    config.putString("broker-list", brokerList) ;
    server.deployVerticle(HttpServerVerticle.class, config, 3);
    Thread.currentThread().join() ; 
  }
}