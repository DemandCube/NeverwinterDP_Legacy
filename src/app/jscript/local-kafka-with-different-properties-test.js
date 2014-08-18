ScriptRunner.require("cluster/RingBearerCluster.js");

var appDir = java.lang.System.getProperty("app.dir") ;

var MAX_DURATION = 60000 ;
var KAFKA_BROKER = "127.0.0.1:9092";
var MESSAGE_SIZE = 1024 ; 

function submitRingBearerJob(jsFile, description, property) {
  cluster.ClusterGateway.execute({
    command: "ringbearer submit " + 
             "  --member-role ringbearer" +
             "  -PMAX_DURATION="  + MAX_DURATION +
             "  -PKAFKA_BROKER="  + KAFKA_BROKER +
             "  -PMESSAGE_SIZE="  + MESSAGE_SIZE +
             "  --file " + jsFile +
             "  -PPROPERTY="   + property +
             "  --description \"" + description + "\"",

    onResponse: function(resp) {
      new cluster.ResponsePrinter(console, resp).print();
      Assert.assertTrue(resp.success && !resp.isEmpty()) ;
    }
  }) ;
} ;

submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "Default", "");
submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "request.required.acks=1", "request.required.acks=1");
submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "request.required.acks=-1", "request.required.acks=-1");
submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "producer.type=async", "producer.type=async") ;
submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "compression.codec=gzip", "compression.codec=gzip") ;
submitRingBearerJob(appDir + "/jscript/ringbearer/job/kafka/kafka-property-job.js", "compression.codec=snappy", "compression.codec=snappy") ;




