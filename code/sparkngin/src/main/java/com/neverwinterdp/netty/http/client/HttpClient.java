package com.neverwinterdp.netty.http.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

public class HttpClient {
  private String host ;
  private int    port ;
  private Channel channel ;
  private EventLoopGroup group ;
  
  public HttpClient(String host, int port, final ResponseHandler handler) throws Exception {
    this.host = host ;
    this.port = port ;
    ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
      public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        //p.addLast("log", new LoggingHandler(LogLevel.INFO));
        p.addLast("codec", new HttpClientCodec());
        //handle automatic content decompression.
        p.addLast("inflater", new HttpContentDecompressor());
        //handle HttpChunks.
        p.addLast("aggregator", new HttpObjectAggregator(1048576));
        p.addLast("handler", new HttpClientHandler(handler));
      }
    };
    
    group = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();
    b.group(group).
      channel(NioSocketChannel.class).
      handler(initializer);
    // Make the connection attempt.
    channel = b.connect(host, port).sync().channel();
  }
  
  public void close() {
    //Shut down executor threads to exit.
    group.shutdownGracefully();
  }
  
  public ChannelFuture closeFuture() throws InterruptedException {
    // Wait for the server to close the connection.
    return channel.closeFuture().sync();
  }
  
  public void get(String uriString) throws Exception {
    URI uri = new URI(uriString);
    channel.writeAndFlush(createRequest(uri, HttpMethod.GET));
  }
  
  public void post(String uriString) throws Exception {
    URI uri = new URI(uriString);
    channel.writeAndFlush(createRequest(uri, HttpMethod.POST));
  }
  
  HttpRequest createRequest(URI uri, HttpMethod method) {
   // Prepare the HTTP request.
    if(uri.getHost() != null && !host.equalsIgnoreCase(uri.getHost())) {
      throw new RuntimeException("expect uri with the host " + host) ;
    }
    if(uri.getPort() > 0 && port != uri.getPort()) {
      throw new RuntimeException("expect the port in uri = " + port) ;
    }
    HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.toString());
    request.headers().set(HttpHeaders.Names.HOST, host);
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
  
  static public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private ResponseHandler responseHandler ;
    
    public HttpClientHandler(ResponseHandler handler) {
      this.responseHandler = handler ;
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
      if (!(msg instanceof HttpResponse)) return ;
      responseHandler.onResponse((HttpResponse) msg) ;
    }
  }
}
