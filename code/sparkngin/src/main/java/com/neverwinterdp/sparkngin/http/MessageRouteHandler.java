package com.neverwinterdp.sparkngin.http;

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
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.netty.http.route.RouteHandlerGeneric;
import com.neverwinterdp.sparkngin.SendAck;
import com.neverwinterdp.util.JSONSerializer;

public class MessageRouteHandler extends RouteHandlerGeneric {
  protected void doPost(ChannelHandlerContext ctx, HttpRequest httpReq) {
    FullHttpRequest req = (FullHttpRequest) httpReq ;
    ByteBuf byteBuf = req.content() ;
    byte[] data = new byte[byteBuf.readableBytes()] ;
    byteBuf.readBytes(data) ;
    Message message = JSONSerializer.INSTANCE.fromBytes(data, Message.class) ;
    SendAck ack = new SendAck() ;
    ack.setStatus(SendAck.Status.OK);
    writeOK(ctx, httpReq, ack, "application/json") ;
  }
  
  protected <T> void writeOK(ChannelHandlerContext ctx, HttpRequest req, T obj, String contentType) {
    byte[] data = JSONSerializer.INSTANCE.toBytes(obj) ;
    ByteBuf content = Unpooled.wrappedBuffer(data) ;
    writeOK(ctx, req, content, contentType) ;
  }
  
  protected void writeOK(ChannelHandlerContext ctx, HttpRequest req, ByteBuf content, String contentType) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
    response.headers().set(CONTENT_TYPE, contentType);
    response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

    boolean keepAlive = isKeepAlive(req);
    if (!keepAlive) {
      ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      response.headers().set(CONNECTION, Values.KEEP_ALIVE);
      ctx.write(response);
    }
  }
}
