ScriptRunner.require("cluster/DemandSpikeCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var jobFiles = [
  "demandspike-hello-job.json", "demandspike-kafka-load-test-job.json"
];

for(var i = 0; i < jobFiles.length; i++) {
  var jobFile = appDir + "/jscript/" + jobFiles[i] ;
  cluster.ClusterGateway.execute({
    command: "demandspike submit --member-name demandspike1 --file " + jobFile,
    onResponse: function(resp) {
      console.printJSON(resp) ;
    }
  }) ;
}
