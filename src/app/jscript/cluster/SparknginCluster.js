ScriptRunner.require("classpath:util/io.js");

function SparknginCluster(config) {
  this.SPARKNGIN_DEFAULT_CONFIG = {
    serverRole: "sparkngin", 
    servers: ["sparkngin1"],
    httpListenPort: 8080,
    forwarderClass: "com.neverwinterdp.sparkngin.http.NullDevMessageForwarder",
    kafkaBroker: "127.0.0.1:9092",
  };

  this.config =  config != undefined ? config : this.SPARKNGIN_DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module sparkngin on the sparkngin role servers") ;
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module Sparkngin" +
      "  -Pmodule.data.drop=true" +
      "  -Psparkngin:http-listen-port=" + this.config.httpListenPort +
      "  -Psparkngin:forwarder-class=" + this.config.forwarderClass +
      "  -Psparkngin:forwarder-reconnect=30000" +
      "  -Pkafka-producer:metadata.broker.list=" + this.config.kafkaBroker
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module Sparkngin on the " + server + " server") ;
      SHELL.exec(
        "module install " +
        "  --member-name " +  server +
        "  --autostart --module Sparkngin" +
        "  -Pmodule.data.drop=true" +
        "  -Psparkngin:http-listen-port=" + (this.config.httpListenPort + i) +
        "  -Psparkngin:forwarder-class=" + this.config.forwarderClass +
        "  -Psparkngin:forwarder-reconnect=30000" +
        "  -Pkafka-producer:metadata.broker.list=" + this.config.kafkaBroker
      ) ;
    }
  };

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module Sparkngin --timeout 20000"
    ) ;
  };
}
