package com.neverwinterdp.netty.http.route;

import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public interface RouteHandler {
  public void setLogger(Logger logger) ;
  public void handle(ChannelHandlerContext ctx, HttpRequest request) ;
}
