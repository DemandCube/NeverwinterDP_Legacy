ScriptRunner.require("cluster/ClusterShell.js");

function DemandSpikeCluster(config) {
  this.DEMAND_SPIKE_DEFAULT_CONFIG = {
    serverRole: "demandspike", 
    servers: ["demandspike1"]
  };

  this.config =  config != undefined ? config : this.DEMAND_SPIKE_DEFAULT_CONFIG ;

  this.installByRole = function() {
    var params = { 
      "member-role": "demandspike",  "autostart": true, "module": ["DemandSpike"],
      "-Pmodule.data.drop": "true" 
    }
    console.h1("Install the module sparkngin on the demandspike role servers") ;
    ClusterShell.server.install(params) ;
  };

  this.installByServer = function() {
    for(var i = 0; i < this.config.servers.length; i++) {
      var server = this.config.servers[i] ;
      var params = { 
        "member-name": server,  "autostart": true, "module": ["DemandSpike"],
        "-Pmodule.data.drop": "true" 
      };
      console.h1("Install the module Zookeeper on the " + server + " server") ;
      ClusterShell.server.install(params) ;
    }
  };

  this.uninstall = function() {
    var params = { "member-role": "demandspike",  "module": ["DemandSpike"], "timeout": 20000 } ;
    ClusterShell.server.uninstall(params) ;
  };

  this.submitDemandSpikeJob = function(params, wait) {
    cluster.ClusterGateway.plugin('demandspike','submit', {
      params: params,
      onResponse: function(resp) {
        console.h1("Submit a demandspike job");
        new cluster.ResponsePrinter(console, resp).print();
        Assert.assertTrue(resp.success && !resp.isEmpty()) ;
      }
    });
    if(wait) {
      console.h1("Wait for " + params["max-duration"] + "ms");
      java.lang.Thread.sleep(params["max-duration"]);
    }
  } ;

  this.metric = function() {
    var params = { "member-role": this.config.serverRole, "filter": "*DemandSpike*" }
    ClusterShell.server.metric(params) ;
  };
}
