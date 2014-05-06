package com.neverwinterdp.sparkngin.jetty;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServer {
  Server server ;
  
  public HttpServer(int port) {
    server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addServlet(new ServletHolder(new JSONMessageServlet()), "/message/*");

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] { context });

    server.setHandler(contexts);
  }
  
  public void start() throws Exception {
    server.start();
  }
  
  public void join() throws Exception {
    server.join() ;
  }
  
  public void stop() throws Exception {
    server.stop() ;
  }
  
  public static void main(String[] args) throws Exception {
    HttpServer server = new HttpServer(8080);
    server.start() ;
    System.out.println("Start.....................");
  }
}
