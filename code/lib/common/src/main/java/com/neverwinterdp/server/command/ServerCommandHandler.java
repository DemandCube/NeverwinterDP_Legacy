package com.neverwinterdp.server.command;

public interface ServerCommandHandler<T> {
  public void onFinish(ServerCommandHandler<T> command, T result) ;
}
