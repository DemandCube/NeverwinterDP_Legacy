package com.neverwinterdp.queuengin.kafka.cluster;

import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.AbstractService;

public class KafkaConsumerTopicReportService extends AbstractService {
  private KafkaMessageConsumerConnector consumer ;
  
  public void onInit(Server server) {
    super.onInit(server);
  }

  public void start() throws Exception {
    ServiceConfig config = getServiceConfig();
    String topic = config.getParameter("topic", null) ;
    String consumerGroup = config.getParameter("consumerGroup", "KafkaConsumer") ;
    String zookeeperUrls = config.getParameter("zookeeperUrls", "127.0.0.1:2181") ;
    int    numberOfThreads = config.getParameter("threads", 1) ;
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    consumer = new KafkaMessageConsumerConnector(consumerGroup, zookeeperUrls) ;
    consumer.consume(topic, handler, numberOfThreads) ;
  }

  public void stop() {
    consumer.close(); 
  }
}