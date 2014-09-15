ScriptRunner.require("classpath:util/io.js");

function KafkaCluster(config) {
  this.KAFKA_DEFAULT_CONFIG = {
    port: 9092, 
    zookeeperConnect: "127.0.0.1:2181",
    serverRole: "kafka", 
    servers: ["kafka1"]
  };

  this.config =  config != undefined ? config : this.KAFKA_DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module Kafka on the kafka role servers") ;
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module Kafka --timeout 30000" +
      "  -Pmodule.data.drop=true" +
      //"-Pkafka:broker.id": "1",
      "  -Pkafka:port=" + this.config.port +
      "  -Pkafka:zookeeper.connect=" + this.config.zookeeperConnect +

      "  -Pkafka:default.replication.factor=2" +
      "  -Pkafka:controller.socket.timeout.ms=90000" +
      "  -Pkafka:controlled.shutdown.enable=true" +
      "  -Pcontrolled.shutdown.max.retries=3" +
      "  -Pkafka:controlled.shutdown.retry.backoff.ms=60000"
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module Kafka on the " + server + " server") ;
      SHELL.exec(
        "module install " +
        "  --member-name " + server +
        "  --autostart --module Kafka --timeout 30000" +
        "  -Pmodule.data.drop=true" +
        //"-Pkafka:broker.id": "1",
        "  -Pkafka:port=" + (this.config.port + i) +
        "  -Pkafka:zookeeper.connect=" + this.config.zookeeperConnect +
        "  -Pkafka:default.replication.factor=2" +
        "  -Pkafka:controller.socket.timeout.ms=3000" +
        "  -Pcontrolled.shutdown.max.retries=3" +
        "  -Pkafka:controlled.shutdown.retry.backoff.ms=3000"
      ) ;
    }
  };

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module Kafka --timeout 40000"
    ) ;
  };

  this.metric = function() {
    var params = { "member-role": this.config.serverRole, "filter": "*Kafka*" }
    ClusterShell.server.metric(
      "server metric" +
      "  --member-role " + this.config.serverRole +
      "  --filter *Kafka*"
    ) ;
  };
}
