package com.neverwinterdp.sparkngin.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class SparknginServer {
  private final int port;
  private Channel channel ;
  
  public SparknginServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    // Configure the server.
    EventLoopGroup bossGroup   = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.option(ChannelOption.SO_BACKLOG, 1024);
      b.group(bossGroup, workerGroup).
        channel(NioServerSocketChannel.class).
        childHandler(new HttpHelloWorldServerInitializer());

      channel = b.bind(port).sync().channel();
      channel.closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  public void shutdown() {
    channel.close() ;
    System.out.println("Shutdown server!");
  }
  
  static public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    public void initChannel(SocketChannel ch) throws Exception {
      ChannelPipeline p = ch.pipeline();
      p.addLast("codec", new HttpServerCodec());
      p.addLast("handler", new PingServerHandler());
    }
  }
  
  public static void main(String[] args) throws Exception {
    new SparknginServer(8080).run();
  }
}