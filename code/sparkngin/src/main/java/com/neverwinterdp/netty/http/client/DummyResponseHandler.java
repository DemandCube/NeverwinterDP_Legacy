package com.neverwinterdp.netty.http.client;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class DummyResponseHandler implements ResponseHandler {
  public void onResponse(HttpResponse response) {
    printHeaders(response) ;
    printContent(response) ;
  }
  
  private void printHeaders(HttpResponse response) {
    System.out.println("STATUS: " + response.getStatus());
    System.out.println("VERSION: " + response.getProtocolVersion());
    System.out.println();

    if (!response.headers().isEmpty()) {
      for (String name : response.headers().names()) {
        for (String value : response.headers().getAll(name)) {
          System.out.println("HEADER: " + name + " = " + value);
        }
      }
      System.out.println();
    }
  }
  
  private void printContent(HttpResponse response) {
    if (HttpHeaders.isTransferEncodingChunked(response)) {
      System.out.println("CHUNKED CONTENT {");
    } else {
      System.out.println("CONTENT {");
    }

    if(response instanceof HttpContent) {
      HttpContent content = (HttpContent) response;
      System.out.print(content.content().toString(CharsetUtil.UTF_8));
      System.out.flush();
      if (content instanceof LastHttpContent) {
        System.out.println("\n} END OF CONTENT");
      }
    }
  }
}
