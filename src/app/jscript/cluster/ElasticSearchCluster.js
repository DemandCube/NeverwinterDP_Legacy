ScriptRunner.require("cluster/ClusterShell.js");

function ElasticSearchCluster(config) {
  this.DEFAULT_CONFIG = {
    serverRole: "elasticsearch", 
    servers: ["elasticsearch"]
  };

  this.config =  config != undefined ? config : this.DEFAULT_CONFIG ;

  this.installByRole = function() {
    var params = { 
      "member-role": this.config.serverRole,  "autostart": true, "module": ["ElasticSearch"],
      "-Pmodule.data.drop": "true"
    };
    console.h1("Install the module ElasticSearch by server role " + this.config.serverRole) ;
    ClusterShell.server.install(params) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      var params = { 
        "member-name": server,  "autostart": true, "module": ["ElasticSearch"],
        "-Pmodule.data.drop": "true"
      };
      console.h1("Install the module ElasticSearch by server name " + server) ;
      ClusterShell.server.install(params) ;
    }
  };

  this.uninstall = function() {
    var params = { 
      "member-role": this.config.serverRole,  "module": ["ElasticSearch"], "timeout": 20000 
    }
    ClusterShell.server.uninstall(params) ;
  };

  this.metric = function() {
    var params = { "member-role": this.config.serverRole }
    ClusterShell.server.metric(params) ;
  };
}
