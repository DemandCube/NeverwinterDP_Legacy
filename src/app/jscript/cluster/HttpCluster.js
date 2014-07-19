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
    console.h1("Install the module HttpGateway by server role " + this.config.serverRole) ;
    ClusterShell.module.install(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module HttpGateway" +
      "  -Pmodule.data.drop=true" +
      "  -Phttp-listen-port=" + this.config.listenPort +
      "  -Phttp-www-dir=" + this.config.webappDir

    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module HttpGateway by server name " + server) ;
      ClusterShell.module.install(
        "module install " +
        "  --member-name " +  server +
        "  --autostart --module HttpGateway" +
        "  -Pmodule.data.drop=true" +
        "  -Phttp-listen-port=" + (this.config.listenPort + i) +
        "  -Phttp-www-dir=" + this.config.webappDir
      ) ;
    }
  } ;

  this.uninstall = function() {
    ClusterShell.module.uninstall(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module HttpGateway --timeout 20000"
    ) ;
  };

  this.metric = function() {
    var params = { "member-role": this.config.serverRole }
    ClusterShell.server.metric(
      "server metric --member-role " + this.config.serverRole
    ) ;
  };
}
