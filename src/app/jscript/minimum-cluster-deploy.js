ScriptRunner.require("cluster/ElasticSearchCluster.js");
ScriptRunner.require("cluster/HttpCluster.js");
ScriptRunner.require("cluster/ZookeeperCluster.js");
ScriptRunner.require("cluster/KafkaCluster.js");
ScriptRunner.require("cluster/KafkaConsumerCluster.js");
ScriptRunner.require("cluster/SparknginCluster.js");
ScriptRunner.require("cluster/RingBearerCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var HTTP_CONFIG = {
  listenPort: 8080, 
  webappDir: appDir + "/webapp",
  serverRole: "generic", 
  servers: ["generic"]
};

/*
var ES_CONFIG = {
  serverRole: "elasticsearch", 
  servers: ["elasticsearch-1"]
};
*/

var ES_CONFIG = {
  serverRole: "generic", 
  servers: ["generic"]
};

var ZOOKEEPER_CONFIG = {
  listenPort: 2181, serverRole: "zookeeper", servers: ["zookeeper-1"]
};

var KAFKA_CONFIG = {
  port: 9092, 
  zookeeperConnect: "zookeeper-1:2181",
  serverRole: "kafka", 
  servers: ["kafka-1", "kafka-2"],
  kafkaConnect: "kafka-1:9092,kafka-2:9092"
};

var KAFKA_CONSUMER_CONFIG = {
  serverRole: "generic", 
  servers: ["generic"],
  zookeeperConnect: "zookeeper-1:2181"
};


var SPARKNGIN_CONFIG = {
  serverRole: "sparkngin", 
  servers: ["sparkngin-1"],
  httpListenPort: 7080,
  forwarderClass: "com.neverwinterdp.sparkngin.http.KafkaMessageForwarder",
  kafkaBroker: KAFKA_CONFIG.kafkaConnect,
  sparknginConnect: "sparkngin-1:7080"
};

var RING_BEARER_CONFIG = {
  serverRole: "generic",
  servers: ["generic"]
};

var ClusterEnv = {
  httpCluster: new HttpCluster(HTTP_CONFIG) ,
  esCluster: new ElasticSearchCluster(ES_CONFIG) ,
  zkCluster: new ZookeeperCluster(ZOOKEEPER_CONFIG) ,
  kafkaCluster: new KafkaCluster(KAFKA_CONFIG),
  kafkaConsumerCluster: new KafkaConsumerCluster(KAFKA_CONSUMER_CONFIG),
  sparknginCluster: new SparknginCluster(SPARKNGIN_CONFIG),
  ringbearerCluster: new RingBearerCluster(RING_BEARER_CONFIG),

  install: function() {
    this.httpCluster.installByServer() ;
    this.esCluster.installByServer() ;
    this.zkCluster.installByServer() ;
    this.kafkaCluster.installByServer() ;
    this.kafkaConsumerCluster.installByServer() ;
    this.sparknginCluster.installByServer() ;
    this.ringbearerCluster.installByServer() ;
    ClusterShell.module.list("module list") ;
  },

  uninstall: function() {
    this.ringbearerCluster.uninstall() ;
    this.sparknginCluster.uninstall() ;
    this.kafkaCluster.uninstall() ;
    this.kafkaConsumerCluster.uninstall() ;
    this.zkCluster.uninstall() ;
    this.httpCluster.uninstall() ;
    this.esCluster.uninstall() ;
  }
}

ClusterEnv.install() ;
