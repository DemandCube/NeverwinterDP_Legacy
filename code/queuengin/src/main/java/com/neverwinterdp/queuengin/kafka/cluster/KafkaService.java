package com.neverwinterdp.queuengin.kafka.cluster;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.Time;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.AbstractService;

public class KafkaService extends AbstractService {
  private String configDir;
  private KafkaServer server ;
  
  public void onInit(Server server) {
    super.onInit(server);
    configDir = server.getRuntimeEnvironment().getConfigDir();
  }

  public void start() {
    ServiceConfig config = getServiceConfig();
    try {
      String kafkaConfigPath = (String)config.getParameters().get("kafkaConfigPath") ;
      kafkaConfigPath = configDir + "/" + kafkaConfigPath ;
      Properties props = new Properties();
      props.load(new FileInputStream(kafkaConfigPath));
      String logDir = props.getProperty("log.dirs") ;
      logDir = logDir.replace("/", File.separator) ;
      props.setProperty("log.dirs", logDir) ;
      server = new KafkaServer(new KafkaConfig(props), new SystemTime());
      server.startup();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    server.shutdown();
  }
  
  static public class SystemTime implements Time {
    public long milliseconds() {
      return System.currentTimeMillis();
    }
    public long nanoseconds() {
      return System.nanoTime();
    }
    @Override
    public void sleep(long ms) {
      try {
        Thread.sleep(ms);
      } catch (InterruptedException e) {
      }
    }
  }
}