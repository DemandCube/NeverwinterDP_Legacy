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
    ClusterShell.module.list() ;
  },

  uninstall: function() {
    this.demandspikeCluster.uninstall() ;
    this.sparknginCluster.uninstall() ;
    this.kafkaCluster.uninstall() ;
    this.zkCluster.uninstall() ;
    //this.httpCluster.uninstall() ;
    //this.esCluster.uninstall() ;
  }
}

function runAll() {
  var demandSpikeJobParams = [
    {
      //test kafka with 1k message size
      "driver": "kafka", "broker-connect": KAFKA_CONFIG.kafkaConnect, "topic": "metrics.consumer", 
      "num-of-task": 2,  "num-of-thread": 2, "message-size": 1024,
      "member-role": "demandspike", "max-duration": 15000, "max-num-of-message": 3000000
    },
    {
      //test kafka with 2k message size
      "driver": "kafka", "broker-connect": KAFKA_CONFIG.kafkaConnect, "topic": "metrics.consumer", 
      "num-of-task": 2,  "num-of-thread": 2, "message-size": 2048,
      "member-role": "demandspike", "max-duration": 15000, "max-num-of-message": 3000000
    },
    {
      //test sparkngin with 1k message size, forward to device null
      "driver": "sparkngin", "broker-connect": SPARKNGIN_CONFIG.sparknginConnect, "topic": "metrics.consumer", 
      "num-of-task": 2,  "num-of-thread": 2, "message-size": 1024,
      "member-role": "demandspike", "max-duration": 15000, "max-num-of-message": 3000000
    }
  ]

  for(var i = 0; i < demandSpikeJobParams.length; i++) {
    var jobParams = demandSpikeJobParams[i] ;
    ClusterEnv.install() ;
    ClusterEnv.demandspikeCluster.submitDemandSpikeJob(jobParams, true) ;
    ClusterShell.server.metric({}) ;
    ClusterShell.server.clearMetric({"expression": "*"}) ;
    ClusterEnv.uninstall() ;
  }
}

function runSingle() {
  var jobParams = {
    //test kafka with 1k message size
    "driver": "kafka", "driver:request.required.acks": "1",
    "broker-connect": KAFKA_CONFIG.kafkaConnect, "topic": "metrics.consumer", 
    "broker-connect": KAFKA_CONFIG.kafkaConnect, "topic": "metrics.consumer", 
    "num-of-task": 2,  "num-of-thread": 2, "message-size": 1024,
    "member-role": "demandspike", "max-duration": 60000, "max-num-of-message": 3000000
  }

  ClusterEnv.install() ;
  ClusterEnv.demandspikeCluster.submitDemandSpikeJob(jobParams, true) ;
  ClusterShell.server.metric({}) ;
  ClusterShell.server.clearMetric({"expression": "*"}) ;
  ClusterEnv.uninstall() ;
}

function runKafkaRandomFailure() {
  var jobParams = {
    //test kafka with 1k message size
    "driver": "kafka", "driver:request.required.acks": "1",
    "broker-connect": KAFKA_CONFIG.kafkaConnect, "topic": "metrics.consumer", 
    "num-of-task": 2, "num-of-thread": 2, "message-size": 1024,
    "member-role": "demandspike", "max-duration": 60000, "max-num-of-message": 3000000,

    "problem:kafka.problem": "service-failure",
    "problem:kafka.member-role": "kafka",
    "problem:kafka.module": "Kafka",
    "problem:kafka.service-id": "KafkaClusterService",
    "problem:kafka.period": "15000",
    "problem:kafka.failure-time": "1000"
  }

  ClusterEnv.install() ;
  ClusterEnv.demandspikeCluster.submitDemandSpikeJob(jobParams, true) ;
  ClusterShell.server.metric({}) ;
  ClusterShell.server.clearMetric({"expression": "*"}) ;
  ClusterEnv.uninstall() ;
}

runSingle() ;
//ClusterShell.server.clearMetric({"expression": "*"}) ;
//runKafkaRandomFailure() ;
//runAll() ;

