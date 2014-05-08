package com.neverwinterdp.sparkngin.jetty;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.queuengin.kafka.KafkaJSONMessageProducer;
import com.neverwinterdp.sparkngin.SparkAcknowledge;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.JSONSerializer;

public class JSONMessageServlet extends HttpServlet {
  private KafkaJSONMessageProducer producer ;
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config) ;
    String kafkaConnectionUrls = "127.0.0.1:9090,127.0.0.1:9091" ; 
    producer = new KafkaJSONMessageProducer(kafkaConnectionUrls) ;
  }
  
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json");
    SparkAcknowledge ack = new SparkAcknowledge() ;
    try {
      String topic = getTopic(req) ;
      InputStream is = req.getInputStream() ;
      String json = IOUtil.getStreamContentAsString(is, "UTF-8") ;
      JSONMessage<?> jsonMessage = JSONSerializer.INSTANCE.fromString(json, JSONMessage.class) ;
      if(jsonMessage.isLogEnable()) {
        req.getLocalPort() ;
        jsonMessage.addLog("JSONMessageServlet", "forward by http server, ip = " + req.getLocalAddr() + ", port " + req.getLocalPort()) ;
      }
      producer.send(topic, jsonMessage) ;
      ack.setStatus(SparkAcknowledge.Status.OK) ;
    } catch (Exception e) {
      ack.setStatus(SparkAcknowledge.Status.ERROR) ;
      ack.setMessage(e.getMessage()) ;
    }
    resp.setStatus(HttpServletResponse.SC_OK);
    resp.getOutputStream().write(JSONSerializer.INSTANCE.toBytes(ack)) ;
  }
  
  private String getTopic(HttpServletRequest req) {
    String path  = req.getPathInfo() ;
    if(path == null) return "unknown-topic" ;
    return path.substring(1, path.length()) ;
  }
}