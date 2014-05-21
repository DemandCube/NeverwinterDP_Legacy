package com.neverwinterdp.netty.http.route;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;

public class PingRouteHandler extends RouteHandlerGeneric {
  final static public String REPLY_MESSAGE = "Got your message!" ;
  
  protected void doGet(ChannelHandlerContext ctx, HttpRequest httpReq) {
    boolean keepAlive = isKeepAlive(httpReq);
    ByteBuf contentBuf = Unpooled.wrappedBuffer(REPLY_MESSAGE.getBytes()) ;
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, contentBuf);
    response.headers().set(CONTENT_TYPE, "text/plain");
    response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

    if (!keepAlive) {
      ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      response.headers().set(CONNECTION, Values.KEEP_ALIVE);
      ctx.write(response);
    }
  }
}