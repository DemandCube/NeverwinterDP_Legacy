package com.neverwinterdp.sparkngin.vertx;

import java.net.URL;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;
import org.vertx.java.platform.Verticle;

public class PingVerticle extends Verticle {
  public void start() {
    vertx.eventBus().registerHandler("ping", new Handler<Message<String>>() {
      @Override
      public void handle(Message<String> message) {
        message.reply("pong!");
        System.out.println("Sent back pong");
      }
    });
    container.logger().info("PingVerticle started");
  }

  static public void main(String[] args) throws Exception {
    PlatformManager pm = PlatformLocator.factory.createPlatformManager();
    JsonObject conf = new JsonObject();
    conf.putString("main","com.neverwinterdp.sparkngin.vertx.PingVerticle");
    conf.putBoolean("auto-redeploy", true);
    URL[] cpurl = { new URL("file:build/classes") } ;
    
    pm.deployVerticle("com.neverwinterdp.sparkngin.vertx.PingVerticle", conf, cpurl, 10, null, new Handler<AsyncResult<String>>() {
      public void handle(AsyncResult<String> event) {
        if (event.succeeded()) {
          System.out.println("Deployment ID is " + event.result());
        } else {
          event.cause().printStackTrace();
        }
      }
    });
    
    Thread.sleep(3000);
    Vertx vertx = VertxFactory.newVertx() ; 
    vertx.eventBus().publish("ping", "ping!") ;
    vertx.eventBus().send("ping", "ping!", new Handler<Message<String>>() {
      public void handle(Message<String> reply) {
        System.out.println("Received reply: " + reply.body());
      }
    });
    System.out.println("done!!!!!!!!!!") ;
    
//    pm.deployModule("com.neverwinterdp~sparkngin~1.0", conf, 10, new AsyncResultHandler<String>() {
//      public void handle(AsyncResult<String> asyncResult) {
//        if (asyncResult.succeeded()) {
//          System.out.println("Deployment ID is " + asyncResult.result());
//        } else {
//          asyncResult.cause().printStackTrace();
//        }
//      }
//    });
    //Thread.currentThread().join();
  }
}
