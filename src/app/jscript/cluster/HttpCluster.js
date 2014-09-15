ScriptRunner.require("classpath:util/io.js");

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
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module Http" +
      "  -Pmodule.data.drop=true" +
      "  -Phttp:port=" + this.config.listenPort +
      "  -Phttp:www-dir=" + this.config.webappDir +
      "  -Phttp:route.names=cluster-rest,yarn-app-history" +
      "  -Phttp:route.cluster-rest.handler=com.neverwinterdp.server.gateway.http.HttpGatewayRouteHandler" +
      "  -Phttp:route.cluster-rest.path=/cluster/rest" +
      "  -Phttp:route.yarn-app-history.handler=com.neverwinterdp.hadoop.yarn.app.history.AppHistoryRouteHandler" +
      "  -Phttp:route.yarn-app-history.path=/yarn-app/history.*" 
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module HttpGateway by server name " + server) ;
      SHELL.exec(
        "module install " +
        "  --member-name " +  server +
        "  --autostart --module Http" +
        "  -Pmodule.data.drop=true" +
        "  -Phttp:port=" + (this.config.listenPort + i) +
        "  -Phttp:www-dir=" + this.config.webappDir +
        "  -Phttp:route.names=cluster-rest,yarn-app-history" +
        "  -Phttp:route.cluster-rest.handler=com.neverwinterdp.server.gateway.http.HttpGatewayRouteHandler" +
        "  -Phttp:route.cluster-rest.path=/cluster/rest" +
      "  -Phttp:route.yarn-app-history.handler=com.neverwinterdp.hadoop.yarn.app.history.AppHistoryRouteHandler" +
      "  -Phttp:route.yarn-app-history.path=/yarn-app/history.*" 
      ) ;
    }
  } ;

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module HttpGateway --timeout 20000"
    ) ;
  };
}
