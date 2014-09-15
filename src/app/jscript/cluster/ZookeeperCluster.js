ScriptRunner.require("classpath:util/io.js");

function ZookeeperCluster(config) {
  this.ZOOKEEPER_DEFAULT_CONFIG = {
    listenPort: 2181, serverRole: "zookeeper", servers: ["zookeeper1"]
  };

  this.config =  config != undefined ? config : this.ZOOKEEPER_DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module Zookeeper on the zookeeper role servers") ;
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module Zookeeper" +
      "  -Pmodule.data.drop=true" +
      "  -Pzk:clientPort=" + this.config.listenPort.toString()
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module Zookeeper on the " + server + " server") ;
      SHELL.exec(
        "module install" +
        "  --member-name " + server +
        "  --autostart --module Zookeeper" +
        "  -Pmodule.data.drop=true" +
        "  -Pzk:clientPort=" + (this.config.listenPort + i)
      ) ;
    }
  };

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall" +
      "  --member-role " +  this.config.serverRole +
      "  --module Zookeeper --timeout 20000" 
    ) ;
  };
}
