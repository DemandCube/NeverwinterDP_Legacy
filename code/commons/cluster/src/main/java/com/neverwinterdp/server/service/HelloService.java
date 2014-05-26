package com.neverwinterdp.server.service;

import com.neverwinterdp.server.Server;

public class HelloService extends AbstractService {
  
  public void onInit(Server server) {
    super.onInit(server);
  }
  
  public void start() {
    logger.info("Start start()");
    logger.info("Activating the HelloService...................");
    logger.info("Finish start()");
  }

  public void stop() {
    logger.info("Start stop()");
    logger.info("Stopping the HelloService......................");
    logger.info("Finish stop()");
  }
}
