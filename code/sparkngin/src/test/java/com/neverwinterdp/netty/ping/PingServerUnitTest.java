package com.neverwinterdp.netty.ping;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PingServerUnitTest {
  private PingServer server ;
  @Before
  public void setup() throws Exception {
    server = new PingServer(8080);
    new Thread() {
      public void run() {
        try {
          server.run() ;
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
    PingClient client = new PingClient() ;
    client.get("http://127.0.0.1:8080/path?test=test"); 
  }
}
