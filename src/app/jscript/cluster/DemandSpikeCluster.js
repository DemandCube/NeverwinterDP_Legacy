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

  this.submitDemandSpikeJob = function(command, waitTime) {
    cluster.ClusterGateway.execute({
      command: command,
      onResponse: function(resp) {
        console.h1("Submit a demandspike job");
        new cluster.ResponsePrinter(console, resp).print();
        Assert.assertTrue(resp.success && !resp.isEmpty()) ;
      }
    });
    if(waitTime) {
      console.h1("Wait for " + waitTime + "ms");
      java.lang.Thread.sleep(waitTime);
    }
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
