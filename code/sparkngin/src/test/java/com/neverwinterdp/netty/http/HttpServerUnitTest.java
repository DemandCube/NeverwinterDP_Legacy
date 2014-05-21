package com.neverwinterdp.netty.http;

import static org.junit.Assert.*;

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
    DummyResponseHandler handler = new DummyResponseHandler() ;
    HttpClient client = new HttpClient ("127.0.0.1", 8080, handler) ;
    //client.get("http://127.0.0.1:8080/ping", true); 
    client.get("/ping"); 
    System.out.println("GET-------------------------");
    for(int i = 0; i < 10; i++) {
      client.post("/ping", "Hello");
    }
    Thread.sleep(1000);
    assertEquals(11, handler.getCount()) ;
  }
}
