package com.neverwinterdp.sparkngin.vertx;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

public class EmbbededVertxServerUnitTest {
  @Test
  public void testEventBusPingVerticle() throws Exception {
    EmbbededVertxServer server = new EmbbededVertxServer() ;
    server.deployVerticle(EventBusPingVerticle.class, 1);
    Thread.sleep(1000);
    
    Vertx vertx = server.getPlatformManager().vertx() ; 
    //vertx.eventBus().publish("ping", "ping!") ;
    vertx.eventBus().send("/ping", "ping!", new Handler<Message<String>>() {
      public void handle(Message<String> reply) {
        Assert.assertEquals(EventBusPingVerticle.REPLY_MESSAGE, reply.body());
        System.out.println("Received reply: " + reply.body());
      }
    });
    System.out.println("done!!!!!!!!!!") ;
    Thread.sleep(1000) ;
  }
  
  @Test
  public void testHttpServerPingVerticle() throws Exception {
    EmbbededVertxServer server = new EmbbededVertxServer() ;
    server.deployVerticle(HttpServerPingVerticle.class, 1);
    Thread.sleep(2000);
    
    Vertx vertx = VertxFactory.newVertx() ;
    HttpClient client = vertx.createHttpClient() ;
    client.setHost("localhost").setPort(8080).getNow("/ping", new Handler<HttpClientResponse>() {
      public void handle(HttpClientResponse event) {
        System.out.println(event.statusMessage()) ; 
        event.bodyHandler(new Handler<Buffer>() {
          public void handle(Buffer data) {
            String message = data.toString() ;
            Assert.assertEquals(HttpServerPingVerticle.REPLY_MESSAGE, message);
            System.out.println("Received from HttpServerPingVerticle: " + message);
          }
        });
      }
    });

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create() ;
    httpClientBuilder.setMaxConnTotal(10) ;
    httpClientBuilder.setMaxConnPerRoute(1) ;
    
    org.apache.http.client.HttpClient httpClient = httpClientBuilder.build();

    for(int i = 0; i < 5; i++) {
      HttpPost postRequest = new HttpPost("http://localhost:8080/ping") ;
      StringEntity input = new StringEntity("{'test': 'this is a test'}");
      input.setContentType("application/json");
      postRequest.setEntity(input);
      HttpResponse postResponse = httpClient.execute(postRequest);
      postRequest.abort()  ;
      System.out.println("post response = " + postResponse.getEntity().toString());
    }
    
    HttpGet getRequest = new HttpGet("http://localhost:8080/ping") ;
    HttpResponse response = httpClient.execute(getRequest);
    System.out.println("get response = " + response.getEntity().toString());
  }
  
  static public class EventBusPingVerticle extends Verticle {
    final static public String REPLY_MESSAGE = "pong!" ;
    
    public void start() {
      vertx.eventBus().registerHandler("ping", new Handler<Message<String>>() {
        @Override
        public void handle(Message<String> message) {
          message.reply(REPLY_MESSAGE);
          System.out.println("Sent back " + REPLY_MESSAGE);
        }
      });
      container.logger().info("PingVerticle started");
    }
  }

  static public class HttpServerPingVerticle extends Verticle {
    final static public String REPLY_MESSAGE = "pong!" ;
    
    public void start() {
      RouteMatcher matcher = new RouteMatcher();
      Handler<HttpServerRequest> postHandler = new Handler<HttpServerRequest>() {
        public void handle(final HttpServerRequest req) {
          req.response().setStatusCode(200);
          req.response().putHeader("Content-Type", "application/json");
          req.response().setChunked(true) ;
          req.bodyHandler(new Handler<Buffer>() {
            public void handle(Buffer event) {
              byte[] bytes = event.getBytes() ;
              System.out.println("post bytes = " + bytes.length);
              req.response().end(REPLY_MESSAGE);
              System.out.println(req.method() +" Send back " + REPLY_MESSAGE);
            }
          });
        }
      };
      Handler<HttpServerRequest> getHandler = new Handler<HttpServerRequest>() {
        public void handle(final HttpServerRequest req) {
          req.response().putHeader("Content-Type", "text/plain");
          req.response().end(REPLY_MESSAGE);
          System.out.println(req.method() +" Send back " + REPLY_MESSAGE);
        }
      };
      matcher.post("/ping", postHandler) ;
      matcher.get("/ping", getHandler) ;
      final HttpServer server = getVertx().createHttpServer();
      server.requestHandler(matcher) ;
      server.listen(8080);
      System.out.println("Server started");
    }
    
  }
}
