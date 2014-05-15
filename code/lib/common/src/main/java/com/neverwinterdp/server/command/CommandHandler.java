package com.neverwinterdp.server.command;

public interface CommandHandler<T> {
  public void onFinish(CommandHandler<T> command, T result) ;
}
