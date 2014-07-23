ScriptRunner.require("cluster/ClusterShell.js");

function DemandSpikeCluster(config) {
  this.DEMAND_SPIKE_DEFAULT_CONFIG = {
    serverRole: "demandspike", 
    servers: ["demandspike1"]
  };

  this.config =  config != undefined ? config : this.DEMAND_SPIKE_DEFAULT_CONFIG ;

  this.installByRole = function() {
    console.h1("Install the module DemandSpike on the demandspike role servers") ;
    ClusterShell.module.install(
      "module install " +
      "  --member-role " + this.config.serverRole +
      "  --autostart --module DemandSpike" +
      "  -Pmodule.data.drop=true" 
    ) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      console.h1("Install the module DemandSpike on the " + server + " server") ;
      ClusterShell.module.install(
        "module install " +
        "  --member-name " + server +
        "  --autostart --module DemandSpike" +
        "  -Pmodule.data.drop=true" 
      ) ;
    }
  };

  this.uninstall = function() {
    console.h1("Uninstall the module DemandSpike on the server role " + this.config.serverRole) ;
    ClusterShell.module.uninstall(
      "module uninstall " +
      "  --member-role " + this.config.serverRole +
      "  --module DemandSpike --timeout 20000"
    ) ;
  };

  this.submitDemandSpikeJob = function(job) {
    var jsonJOB = JSONStringify(job, null, "  ") ;
    cluster.ClusterGateway.execute({
      command: "demandspike submit " + 
               "  --member-role " + this.config.serverRole +
               "  #{data " + jsonJOB + "}#",
      onResponse: function(resp) {
        new cluster.ResponsePrinter(console, resp).print();
        Assert.assertTrue(resp.success && !resp.isEmpty()) ;
      }
    }) ;
  } ;

  this.metric = function() {
    var params = { "member-role": this.config.serverRole, "filter": "*DemandSpike*" }
    ClusterShell.server.metric(
      "server metric " +
      "  --member-role " + this.config.serverRole +
      "  --filter *DemandSpike*"
    ) ;
  };
}
