package com.neverwinterdp.sparkngin.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;

import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.queuengin.kafka.KafkaJSONMessageProducer;
import com.neverwinterdp.sparkngin.SparkAcknowledge;
import com.neverwinterdp.util.JSONSerializer;

public class MessageHandlers  {
  private KafkaJSONMessageProducer producer ;
  
  public void configure(RouteMatcher matcher, JsonObject config) {
    String kafkaConnectionUrls = "127.0.0.1:9090,127.0.0.1:9091" ; 
    producer = new KafkaJSONMessageProducer(kafkaConnectionUrls) ;
    matcher.post("/message/:topic", new Post()) ;
  }
  
  public class Post implements Handler<HttpServerRequest> {
    public void handle(final HttpServerRequest req) {
      req.response().setStatusCode(200);
      req.response().putHeader("Content-Type", "application/json");
      req.response().setChunked(true) ;
      final String topic = req.params().get("topic") ;
      req.bodyHandler(new Handler<Buffer>() {
        public void handle(Buffer buf) {
          SparkAcknowledge ack = new SparkAcknowledge() ;
          byte[] bytes = buf.getBytes() ;
          try {
            JSONMessage<?> jsonMessage = JSONSerializer.INSTANCE.fromBytes(bytes, JSONMessage.class) ;
            if(jsonMessage.isLogEnable()) {
              int port = req.localAddress().getPort() ;
              String addr = req.localAddress().getHostString() ;
              jsonMessage.addLog("JSONMessageServlet", "forward by http server, ip = " + addr + ", port " + port) ;
            }
            producer.send(topic, jsonMessage) ;
            ack.setStatus(SparkAcknowledge.Status.OK) ;
          } catch (Exception e) {
            ack.setStatus(SparkAcknowledge.Status.ERROR) ;
            ack.setMessage(e.getMessage()) ;
          }
          req.response().end(JSONSerializer.INSTANCE.toString(ack));
          System.out.println(buf.toString());
        }
      });
    }
  }
}
