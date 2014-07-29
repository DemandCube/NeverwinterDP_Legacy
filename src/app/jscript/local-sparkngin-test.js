ScriptRunner.require("cluster/DemandSpikeCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var MAX_DURATION = 60000 ;
var SPARKNGIN_BROKER = "127.0.0.1:7080" ;

function submitDemandSpikeJob(jsFile, description, maxDuration, sparknginBroker) {
  cluster.ClusterGateway.execute({
    command: "demandspike submit " + 
             "  --member-role demandspike" +
             "  --description \"" + description + "\"" +
             "  -PMAX_DURATION="  + maxDuration +
             "  -PSPARKNGIN_BROKER="  + sparknginBroker +
             "  --file " + jsFile ,

    onResponse: function(resp) {
      new cluster.ResponsePrinter(console, resp).print();
      Assert.assertTrue(resp.success && !resp.isEmpty()) ;
    }
  }) ;
} ;

submitDemandSpikeJob(appDir + "/jscript/demandspike/job/sparkngin/hello-job.js", "Hello Demandspike Sparkngin Job", MAX_DURATION, SPARKNGIN_BROKER) ;
submitDemandSpikeJob(appDir + "/jscript/demandspike/job/sparkngin/load-test-job.js", "Sparkngin load test", MAX_DURATION, SPARKNGIN_BROKER) ;
