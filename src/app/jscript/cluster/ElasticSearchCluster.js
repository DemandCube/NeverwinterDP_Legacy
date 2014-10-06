ScriptRunner.require("classpath:util/io.js");

function ElasticSearchCluster(config) {
  this.DEFAULT_CONFIG = {
    serverRole: "elasticsearch", 
    servers: ["elasticsearch"]
  };

  this.config =  config != undefined ? config : this.DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module ElasticSearch by server role " + this.config.serverRole) ;
    SHELL.exec(
      "module install" +
      "  --member-role " + this.config.serverRole +
      "  --autostart module ElasticSearch --timeout 30000" +
      "  -Pmodule.data.drop=true"
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module ElasticSearch by server name " + server) ;
      SHELL.exec(
        "module install" +
        "  --member-name " +server +
        "  --autostart --module ElasticSearch --timeout 30000" +
        "  -Pmodule.data.drop=true"
      ) ;
    }
  };

  this.uninstall = function() {
    var params = { 
      "member-role": this.config.serverRole,  "module": ["ElasticSearch"], "timeout": 20000 
    }
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module ElasticSearch --timeout 30000" 
    ) ;
  };
}
