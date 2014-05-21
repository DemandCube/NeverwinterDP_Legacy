package com.neverwinterdp.netty.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neverwinterdp.netty.http.client.DummyResponseHandler;
import com.neverwinterdp.netty.http.client.HttpClient;
import com.neverwinterdp.netty.http.route.PingRouteHandler;

public class HttpServerUnitTest {
  static {
    System.setProperty("log4j.configuration", "file:src/main/resources/log4j.properties") ;
  }
  
  private HttpServer server ;
  
  @Before
  public void setup() throws Exception {
    server = new HttpServer();
    server.add("/ping", new PingRouteHandler());
    new Thread() {
      public void run() {
        try {
          server.start() ;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start() ;
    Thread.sleep(1000);
  }
  
  @After
  public void teardown() {
    server.shutdown() ;
  }
  
  @Test
  public void testGet() throws Exception {
    HttpClient client = 
      new HttpClient ("127.0.0.1", 8080, new DummyResponseHandler()) ;
    client.get("http://127.0.0.1:8080/ping"); 
    client.get("/ping"); 
    client.get("/ping"); 
    client.closeFuture() ;
  }
}
