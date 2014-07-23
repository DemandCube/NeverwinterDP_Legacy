ScriptRunner.require("cluster/ClusterShell.js");

function KafkaConsumerCluster(config) {
  this.DEFAULT_CONFIG = {
    serverRole: "generic", 
    servers: ["generic"]
  };

  this.config =  config != undefined ? config : this.DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module KafkaConsumer") ;
    ClusterShell.module.install(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module KafkaConsumer" +
      "  -Pmodule.data.drop=true"
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module KafkaConsumer on the " + server + " server") ;
      ClusterShell.module.install(
        "module install " +
        "  --member-name " + server +
        "  --autostart --module KafkaConsumer" +
        "  -Pmodule.data.drop=true"
      ) ;
    }
  };

  this.uninstall = function() {
    ClusterShell.module.uninstall(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module KafkaConsumer"
    ) ;
  };
}
