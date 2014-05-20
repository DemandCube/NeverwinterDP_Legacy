package com.neverwinterdp.sparkngin.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.URI;

public class PingClient {
  static public class PingClientInitializer extends ChannelInitializer<SocketChannel> {
    public void initChannel(SocketChannel ch) throws Exception {
      ChannelPipeline p = ch.pipeline();
      p.addLast("log", new LoggingHandler(LogLevel.INFO));
      p.addLast("codec", new HttpClientCodec());
      //handle automatic content decompression.
      p.addLast("inflater", new HttpContentDecompressor());
      //handle HttpChunks.
      p.addLast("aggregator", new HttpObjectAggregator(1048576));
      p.addLast("handler", new PingClientHandler());
    }
  }
  
  public void get(String url) throws Exception {
    URI uri = new URI(url);
    String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
    String host = uri.getHost() == null ? "localhost" : uri.getHost();
    int port = uri.getPort();
    if (port == -1) port = 80;
    
    if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
      System.err.println("Only HTTP(S) is supported.");
      return;
    }
    HttpRequest request = createGetRequest(uri) ;
    send(host, port, request) ;
  }
  
  void send(String host, int port, HttpRequest request) throws Exception {
    // Configure the client.
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group).
        channel(NioSocketChannel.class).
        handler(new PingClientInitializer());

      // Make the connection attempt.
      Channel ch = b.connect(host, port).sync().channel();
      // Send the HTTP request.
      ch.writeAndFlush(request);

      // Wait for the server to close the connection.
      ch.closeFuture().sync();
    } finally {
      // Shut down executor threads to exit.
      group.shutdownGracefully();
    }
  }
  
  HttpRequest createGetRequest(URI uri) {
    System.out.println("GET URI = " + uri.toString());
   // Prepare the HTTP request.
    HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toString());
    request.headers().set(HttpHeaders.Names.HOST, uri.getHost());
    request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
    request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

    // Set some example cookies.
    //request.headers().set(
    //    HttpHeaders.Names.COOKIE,
    //    ClientCookieEncoder.encode(
    //        new DefaultCookie("my-cookie", "foo"),
    //        new DefaultCookie("another-cookie", "bar")
    //    )
    //);
    return request ;
  }
}
