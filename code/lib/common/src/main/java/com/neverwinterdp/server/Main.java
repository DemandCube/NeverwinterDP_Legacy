package com.neverwinterdp.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.neverwinterdp.server.config.Configuration;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.JSONSerializer;

public class Main {
  static public class Options {
    @Parameter(names = "-config", description = "The configuration file in json format")
    String configFile;
  }

  
  static public void main(String[] args) throws Exception {
    if(args == null || args.length == 0) {
      args = new String[] {
        "-config", "classpath:server-default-configuration.json"
      };
    }
    Options options = new Options();
    new JCommander(options, args);
    String jsonConfig = IOUtil.getResourceAsString(options.configFile, "UTF-8") ;
    Configuration conf = JSONSerializer.INSTANCE.fromString(jsonConfig, Configuration.class) ;
    
    Server server = new Server() ;
    server.setConfig(conf.getServer());
    server.onInit();
    server.getServiceContainer().register(conf.getServices());
    server.start();
    Thread.currentThread().join();
  }
}
