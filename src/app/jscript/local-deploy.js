ScriptRunner.require("cluster/ElasticSearchCluster.js");
ScriptRunner.require("cluster/HttpCluster.js");
ScriptRunner.require("cluster/ZookeeperCluster.js");
ScriptRunner.require("cluster/KafkaCluster.js");
ScriptRunner.require("cluster/KafkaConsumerCluster.js");
ScriptRunner.require("cluster/SparknginCluster.js");
ScriptRunner.require("cluster/RingBearerCluster.js");
ScriptRunner.require("module/YaraModule.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var HTTP_CONFIG = {
  listenPort: 8080, 
  //webappDir: appDir + "/webapp",
  webappDir:  appDir + "/../../../src/main/webapp",
  serverRole: "generic", 
  servers: ["generic"]
};

var ES_CONFIG = {
  serverRole: "elasticsearch", 
  servers: ["elasticsearch1", "elasticsearch2"]
};


var KAFKA_CONFIG = {
  port: 9092, 
  zookeeperConnect: "127.0.0.1:2181",
  serverRole: "kafka", 
  servers: ["kafka1", "kafka2", "kafka3"],
  kafkaConnect: "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094"
};

var KAFKA_CONSUMER_CONFIG = {
  serverRole: "generic", 
  servers: ["generic"],
  zookeeperConnect: "127.0.0.1:2181"
};

var SPARKNGIN_CONFIG = {
  serverRole: "sparkngin", 
  servers: ["sparkngin1"],
  httpListenPort: 7080,
  forwarderClass: "com.neverwinterdp.sparkngin.KafkaMessageForwarder",
  kafkaBroker: KAFKA_CONFIG.kafkaConnect,
  sparknginConnect: "127.0.0.1:7080"
};

var YARA_CONFIG = {
  rpcPort: 8463, 
  rpcHost: "127.0.0.1", 
  server: "generic"
};

var ClusterEnv = {
  httpCluster: new HttpCluster(HTTP_CONFIG) ,
  esCluster: new ElasticSearchCluster(ES_CONFIG) ,
  zkCluster: new ZookeeperCluster() ,
  kafkaCluster: new KafkaCluster(KAFKA_CONFIG),
  kafkaConsumerCluster: new KafkaConsumerCluster(KAFKA_CONSUMER_CONFIG),
  sparknginCluster: new SparknginCluster(SPARKNGIN_CONFIG),
  ringbearerCluster: new RingBearerCluster(),
  yaraModule: new YaraModule(YARA_CONFIG),

  install: function() {
    this.httpCluster.installByServer() ;
    this.esCluster.installByServer() ;
    this.zkCluster.installByServer() ;
    this.kafkaCluster.installByServer() ;
    this.kafkaConsumerCluster.installByServer() ;
    this.sparknginCluster.installByServer() ;
    this.ringbearerCluster.installByServer() ;

    this.yaraModule.install() ;
    SHELL.exec("module list") ;
  },

  uninstall: function() {
    this.ringbearerCluster.uninstall() ;
    this.sparknginCluster.uninstall() ;
    this.kafkaCluster.uninstall() ;
    this.kafkaConsumerCluster.uninstall() ;
    this.zkCluster.uninstall() ;
    this.httpCluster.uninstall() ;
    this.esCluster.uninstall() ;

    this.yaraModule.uninstall() ;
  }
}

ClusterEnv.install() ;
