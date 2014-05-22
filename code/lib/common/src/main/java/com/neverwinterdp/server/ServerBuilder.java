package com.neverwinterdp.server;

import com.neverwinterdp.server.config.Configuration;
import com.neverwinterdp.server.config.ServerConfig;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.JSONSerializer;

public class ServerBuilder {
  Configuration conf ;
  
  public ServerBuilder() {
    conf = new Configuration() ;
    ServerConfig serverConfig = new ServerConfig() ;
    serverConfig.setClusterRPC("hazelcast");
    serverConfig.setVersion(1.0f);
    serverConfig.setListenPort(5700);
    conf.setServer(serverConfig);
  }
  
  public ServerBuilder(String configFile) throws Exception {
    String jsonConfig = IOUtil.getFileContentAsString(configFile, "UTF-8") ;
    conf = JSONSerializer.INSTANCE.fromString(jsonConfig, Configuration.class) ;
  }

  public ServiceConfig addService(Class<?> type) {
    ServiceConfig config = new ServiceConfig() ;
    config.setServiceId(type.getSimpleName());
    config.setName(type.getSimpleName());
    config.setClassName(type.getName());
    conf.addService(config) ;
    return config ;
  }
  
  public ServiceConfig addService(String serviceId, String className) {
    ServiceConfig config = new ServiceConfig() ;
    config.setServiceId(serviceId);
    config.setName(serviceId);
    config.setClassName(className);
    conf.addService(config) ;
    return config ;
  }
  
  public Server build() throws Exception {
    Server server = new Server() ;  
    server.setConfig(conf.getServer());
    server.onInit();
    server.getServiceContainer().register(conf.getServices());
    server.start();
    return server ;
  }
}
