ScriptRunner.require("cluster/ClusterShell.js");

function HttpCluster(config) {
  this.HTTP_DEFAULT_CONFIG = {
    listenPort: 8080, 
    webappDir: "webapp",
    serverRole: "generic", 
    servers: ["generic"]
  };

  this.config =  config != undefined ? config : this.HTTP_DEFAULT_CONFIG ;

  this.installByRole = function() {
    var params = { 
      "member-role": this.config.serverRole,  "autostart": true, "module": ["HttpGateway"],
      "-Pmodule.data.drop": "true" ,
      "-Phttp-listen-port": this.config.listenPort.toString(),
      "-Phttp-www-dir": this.config.webappDir
    };
    console.h1("Install the module HttpGateway by server role " + this.config.serverRole) ;
    ClusterShell.server.install(params) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      var params = { 
        "member-name": server,  "autostart": true, "module": ["HttpGateway"],
        "-Pmodule.data.drop": "true" ,
        "-Phttp-listen-port":(this.config.listenPort + i).toString(),
        "-Phttp-www-dir": this.config.webappDir
      };
      console.h1("Install the module HttpGateway by server name " + server) ;
      ClusterShell.server.install(params) ;
    }
  };

  this.uninstall = function() {
    var params = { 
      "member-role": this.config.serverRole,  "module": ["HttpGateway"], "timeout": 20000 
    }
    ClusterShell.server.uninstall(params) ;
  };

  this.metric = function() {
    var params = { "member-role": this.config.serverRole }
    ClusterShell.server.metric(params) ;
  };
}
