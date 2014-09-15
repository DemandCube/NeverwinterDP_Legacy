ScriptRunner.require("classpath:util/io.js");

function KafkaConsumerCluster(config) {
  this.DEFAULT_CONFIG = {
    serverRole: "generic", 
    servers: ["generic"] ,
    zookeeperConnect: "127.0.0.1:2181"
  };

  this.config =  config != undefined ? config : this.DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module KafkaConsumer") ;
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module KafkaConsumer" +
      "  -Pmodule.data.drop=true" +
      "  -Pkafka:zookeeper.connect=" + this.config.zookeeperConnect
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module KafkaConsumer on the " + server + " server") ;
      SHELL.exec(
        "module install " +
        "  --member-name " + server +
        "  --autostart --module KafkaConsumer" +
        "  -Pmodule.data.drop=true" +
        "  -Pkafka:zookeeper.connect=" + this.config.zookeeperConnect
      ) ;
    }
  };

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module KafkaConsumer"
    ) ;
  };
}
