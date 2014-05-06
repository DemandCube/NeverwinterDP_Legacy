package com.neverwinterdp.sparkngin.vertx;

import java.net.URL;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;
import org.vertx.java.platform.Verticle;
import org.vertx.java.platform.impl.cli.Starter ;

public class Server extends Verticle {
  public void start() {
    final HttpServer server = getVertx().createHttpServer();
    server.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(final HttpServerRequest req) {
        req.response().putHeader("Content-Type", "text/plain");
        req.response().end("hello, world");
        System.out.println("Got a request");
      }
    });
    server.listen(8080);
    System.out.println("Server started");
  }
  
  static public void main(String[] args) throws Exception {
    PlatformManager pm = PlatformLocator.factory.createPlatformManager();
    JsonObject conf = new JsonObject();
    conf.putString("main","com.neverwinterdp.sparkngin.vertx.Server");
    conf.putBoolean("auto-redeploy", true);
    URL[] cpurl = { new URL("file:build/classes") } ;
    
    pm.deployVerticle("com.neverwinterdp.sparkngin.vertx.Server", conf, cpurl, 10, null, new Handler<AsyncResult<String>>() {
      public void handle(AsyncResult<String> event) {
        if (event.succeeded()) {
          System.out.println("Deployment ID is " + event.result());
        } else {
          event.cause().printStackTrace();
        }
      }
    });
    
    Thread.sleep(1000);
    System.out.println("start post..............");
    Vertx vertx = VertxFactory.newVertx() ; 
    HttpClient client = vertx.createHttpClient() ;
    client.setHost("localhost").setPort(8080).getNow("/", new Handler<HttpClientResponse>() {
      public void handle(HttpClientResponse event) {
        System.out.println(event.statusMessage()) ; 
      }
    });
    
    System.out.println("end post..............");
    Thread.currentThread().join(); 
  }
}