package com.neverwinterdp.sparkngin.vertx;

import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class HttpServerVerticle extends Verticle {
  final static public String REPLY_MESSAGE = "pong!" ;
  
  public void start() {
    JsonObject config = container.config();
    int listenPort = config.getInteger("http-listen-port") ;
    if(listenPort == 0) listenPort = 8080 ;    
    RouteMatcher matcher = new RouteMatcher();
    new MessageHandlers().configure(matcher, config);
    final HttpServer server = getVertx().createHttpServer();
    server.requestHandler(matcher);
    server.listen(listenPort);
    System.out.println("HTTP Server started on " + listenPort);
  }
}