package com.neverwinterdp.queuengin.kafka.cluster;

import kafka.Kafka;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.AbstractService;

public class KafkaServiceByMain extends AbstractService {
  private String configDir;
  private Thread kafkaThread ;

  public void onInit(Server server) {
    super.onInit(server);
    configDir = server.getRuntimeEnvironment().getConfigDir();
  }

  public void start() {
    kafkaThread = new Thread() {
      public void run() {
        try {
          ServiceConfig config = getServiceConfig();
          String kafkaConfigPath = 
            (String)config.getParameters().get("kafkaConfigPath") ;
          kafkaConfigPath = configDir + "/" + kafkaConfigPath ;
          String[] args = { kafkaConfigPath } ;
          logger.info("launch Kafka.main(...)");
          Kafka.main(args) ;
        } catch(Exception ex) {
          kafkaThread = null ;
          logger.error("Cannot lauch the KafkaService", ex);
          throw new RuntimeException("Cannot lauch the KafkaService", ex);
        }
      }
    };
    kafkaThread.start() ;
  }

  public void stop() {
    if( kafkaThread!= null && kafkaThread.isAlive()) {
      kafkaThread.interrupt() ; 
      kafkaThread = null ;
    }
  }
}