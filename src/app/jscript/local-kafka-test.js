ScriptRunner.require("cluster/DemandSpikeCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;
var MAX_DURATION = 60000 ;
var KAFKA_BROKER = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094" 

function submitDemandSpikeJob(jsFile, description, messageSize, requestAck, replication) {
  cluster.ClusterGateway.execute({
    command: "demandspike submit " + 
             "  --member-role demandspike" +
             "  --description \"" + description + "\"" +
             "  -PMAX_DURATION="  + MAX_DURATION +
             "  -PKAFKA_BROKER="  + KAFKA_BROKER +
             "  -PMESSAGE_SIZE="  + messageSize +
             "  -PREQUEST_ACK="   + requestAck +
             "  -PREPLICATION="   + replication +
             "  --file " + jsFile ,

    onResponse: function(resp) {
      new cluster.ResponsePrinter(console, resp).print();
      Assert.assertTrue(resp.success && !resp.isEmpty()) ;
    }
  }) ;
} ;

submitDemandSpikeJob(appDir + "/jscript/demandspike/job/kafka/hello-job.js", "Hello Demandspike Job",  1024, 1, 2) ;

var jsFile = appDir + "/jscript/demandspike/job/kafka/kafka-message-size-load-test-job.js" ;
for(var i = 1; i <= 3; i++) {
  var desc = "Kafka load test with " + i + "kb message size" ;
  submitDemandSpikeJob(jsFile, desc, 1024 * i, 1, 2) ;
}

jsFile = appDir + "/jscript/demandspike/job/kafka/kafka-request-ack-load-test-job.js" ;
for(var i = 0; i < 2; i++) {
  var desc = "Kafka load test with request ack = " + i  ;
  submitDemandSpikeJob(jsFile, desc, 1024, i, 2) ;
}

jsFile = appDir + "/jscript/demandspike/job/kafka/kafka-replication-load-test-job.js" ;
for(var i = 1; i <= 3; i++) {
  var desc = "Kafka load test with replication = " + i  ;
  submitDemandSpikeJob(jsFile, desc, 1024, 1, i) ;
}
