ScriptRunner.require("cluster/RingBearerCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var MAX_DURATION = 300000 ;
var SPARKNGIN_BROKER = "sparkngin-1:7080" ;

function submitRingBearerJob(jsFile, description, maxDuration, sparknginBroker) {
  cluster.ClusterGateway.execute({
    command: "ringbearer submit " + 
             "  --member-role ringbearer" +
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

submitRingBearerJob(appDir + "/jscript/ringbearer/job/sparkngin/hello-job.js", "Hello RingBearer Sparkngin Job", MAX_DURATION, SPARKNGIN_BROKER) ;
submitRingBearerJob(appDir + "/jscript/ringbearer/job/sparkngin/load-test-job.js", "Sparkngin load test", MAX_DURATION, SPARKNGIN_BROKER) ;
