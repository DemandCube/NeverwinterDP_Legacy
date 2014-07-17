ScriptRunner.require("cluster/ClusterShell.js");

function ZookeeperCluster(config) {
  this.ZOOKEEPER_DEFAULT_CONFIG = {
    listenPort: 2181, serverRole: "zookeeper", servers: ["zookeeper1"]
  };

  this.config =  config != undefined ? config : this.ZOOKEEPER_DEFAULT_CONFIG ;

  this.installByRole = function() {
    var params = { 
      "member-role": this.config.serverRole,  "autostart": true, "module": ["Zookeeper"],
      "-Pmodule.data.drop": "true" ,
      "-Pzk:clientPort": this.config.listenPort.toString()
    };
    console.h1("Install the module Zookeeper on the zookeeper role servers") ;
    ClusterShell.server.install(params) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      var params = { 
        "member-name": server,  "autostart": true, "module": ["Zookeeper"],
        "-Pmodule.data.drop": "true" ,
        "-Pzk:clientPort": (this.config.listenPort + i).toString()
      };
      console.h1("Install the module Zookeeper on the " + server + " server") ;
      ClusterShell.server.install(params) ;
    }
  };

  this.uninstall = function() {
    var params = { 
      "member-role": this.config.serverRole,  "module": ["Zookeeper"], "timeout": 20000 
    }
    ClusterShell.server.uninstall(params) ;
  };

  this.metric = function() {
    var params = { "member-role": this.config.serverRole }
    ClusterShell.server.metric(params) ;
  };
}
