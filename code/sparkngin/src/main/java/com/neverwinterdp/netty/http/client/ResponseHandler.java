package com.neverwinterdp.netty.http.client;

import io.netty.handler.codec.http.HttpResponse;

public interface ResponseHandler {
  public void onResponse(HttpResponse response) ;
}
