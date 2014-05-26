package com.neverwinterdp.scribengin;

import com.neverwinterdp.queuengin.ReportMessageConsumerHandler;
import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;
import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.AbstractService;

public class ScribenginClusterService extends AbstractService {
  KafkaMessageConsumerConnector consumer ;
  
  public void onInit(Server server) {
    super.onInit(server);
  }
  
  public void start() throws Exception {
    ServiceConfig config = getServiceConfig() ;
    String topic = config.getParameter("topic", null);
    if(topic == null) throw new Exception("Topic is not specified!") ; 
    String consumerGroup = config.getParameter("consumerGroup", "consumer." + topic) ;
    String zkUrls = config.getParameter("zookeeperUrls", "127.0.0.1:2181") ;
    ReportMessageConsumerHandler handler = new ReportMessageConsumerHandler() ;
    consumer = new KafkaMessageConsumerConnector(consumerGroup, zkUrls) ;
    consumer.consume(topic, handler, 1) ;
  }

  public void stop() {
    consumer.close() ;
  }
}
