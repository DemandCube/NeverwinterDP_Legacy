package com.neverwinterdp.sparkngin;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.neverwinterdp.queuengin.JSONMessage;
import com.neverwinterdp.util.JSONSerializer;

public class SimpleSparknginClient {
  private String connectionUrl ;
  HttpClient httpClient ;
  
  public SimpleSparknginClient(String connectionUrl) {
    this.connectionUrl = connectionUrl ;
    
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create() ;
    httpClientBuilder.setMaxConnTotal(10) ;
    httpClientBuilder.setMaxConnPerRoute(1) ;
    httpClient = httpClientBuilder.build();
  }
  
  public String getConnectionUrl() { return this.connectionUrl ; }
  
  public <T> SparkAcknowledge sendJSONMessage(String topic, String key, T obj, boolean logEnable) throws Exception {
    String url = connectionUrl + "/message/" + topic ;  
    HttpPost postRequest = new HttpPost(url);
    JSONMessage<T> jsonMessage = new JSONMessage<T>(key, obj, logEnable) ;
    String json = JSONSerializer.INSTANCE.toString(jsonMessage);
    StringEntity input = new StringEntity(json);
    input.setContentType("application/json");
    postRequest.setEntity(input);
    HttpResponse response = null ;
    try {
      response = httpClient.execute(postRequest);
    } finally {
      postRequest.abort();
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream() ;
    response.getEntity().writeTo(out) ;
    SparkAcknowledge ack = JSONSerializer.INSTANCE.fromBytes(out.toByteArray(), SparkAcknowledge.class) ;
    return ack ;
  }
  
  public boolean ping() {
    return true ;
  }
}