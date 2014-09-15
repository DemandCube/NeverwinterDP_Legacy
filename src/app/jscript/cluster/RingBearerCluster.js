ScriptRunner.require("classpath:util/io.js");

function RingBearerCluster(config) {
  this.DEMAND_SPIKE_DEFAULT_CONFIG = {
    serverRole: "ringbearer", 
    servers: ["ringbearer1"]
  };

  this.config =  config != undefined ? config : this.DEMAND_SPIKE_DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module RingBearer on the ringbearer role servers") ;
    SHELL.exec(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module RingBearer" +
      "  -Pmodule.data.drop=true" 
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module RingBearer on the " + server + " server") ;
      SHELL.exec(
        "module install " +
        "  --member-name " + server +
        "  --autostart --module RingBearer" +
        "  -Pmodule.data.drop=true" 
      ) ;
    }
  };

  this.uninstall = function() {
    console.h1("Uninstall the module RingBearer on the server role " + this.config.serverRole) ;
    SHELL.exec(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module RingBearer --timeout 20000"
    ) ;
  };

  this.submitRingBearerJob = function(job) {
    var jsonJOB = JSONStringify(job, null, "  ") ;
    cluster.ClusterGateway.execute({
      command: "ringbearer submit " + 
               "  --member-role " + this.config.serverRole +
               "  #{data " + jsonJOB + "}#",
      onResponse: function(resp) {
        new cluster.ResponsePrinter(console, resp).print();
        Assert.assertTrue(resp.success && !resp.isEmpty()) ;
      }
    }) ;
  } ;
}
