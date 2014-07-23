ScriptRunner.require("cluster/ElasticSearchCluster.js");
ScriptRunner.require("cluster/HttpCluster.js");
ScriptRunner.require("cluster/ZookeeperCluster.js");
ScriptRunner.require("cluster/KafkaCluster.js");
ScriptRunner.require("cluster/SparknginCluster.js");
ScriptRunner.require("cluster/DemandSpikeCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

this.HTTP_CONFIG = {
  listenPort: 8080, 
  //webappDir: appDir + "/webapp",
  webappDir: "/Users/Tuan/Projects/DemandCube/NeverwinterDP/NeverwinterDP/src/main/webapp",
  serverRole: "generic", 
  servers: ["generic"]
};

this.ES_CONFIG = {
  serverRole: "elasticsearch", 
  servers: ["elasticsearch1", "elasticsearch2"]
};


this.KAFKA_CONFIG = {
  port: 9092, 
  zookeeperConnect: "127.0.0.1:2181",
  serverRole: "kafka", 
  servers: ["kafka1", "kafka2", "kafka3"],
  kafkaConnect: "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094"
};

this.SPARKNGIN_CONFIG = {
  serverRole: "sparkngin", 
  servers: ["sparkngin1"],
  httpListenPort: 7080,
  forwarderClass: "com.neverwinterdp.sparkngin.http.NullDevMessageForwarder",
  sparknginConnect: "127.0.0.1:7070"
};

var ClusterEnv = {
  httpCluster: new HttpCluster(HTTP_CONFIG) ,
  esCluster: new ElasticSearchCluster(ES_CONFIG) ,
  zkCluster: new ZookeeperCluster() ,
  kafkaCluster: new KafkaCluster(KAFKA_CONFIG),
  sparknginCluster: new SparknginCluster(SPARKNGIN_CONFIG),
  demandspikeCluster: new DemandSpikeCluster(),

  install: function() {
    this.httpCluster.installByServer() ;
    this.esCluster.installByServer() ;
    this.zkCluster.installByServer() ;
    this.kafkaCluster.installByServer() ;
    this.sparknginCluster.installByServer() ;
    this.demandspikeCluster.installByServer() ;
    ClusterShell.module.list("module list") ;
  },

  uninstall: function() {
    this.demandspikeCluster.uninstall() ;
    this.sparknginCluster.uninstall() ;
    this.kafkaCluster.uninstall() ;
    this.zkCluster.uninstall() ;
    this.httpCluster.uninstall() ;
    this.esCluster.uninstall() ;
  }
}

ClusterEnv.install() ;
