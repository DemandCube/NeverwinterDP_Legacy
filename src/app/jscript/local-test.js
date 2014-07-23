ScriptRunner.require("cluster/DemandSpikeCluster.js");

function submitDemandSpikeJob(job) {
  var jsonJOB = JSON.stringify(job, null, "  ") ;
  cluster.ClusterGateway.execute({
    command: "demandspike submit " + 
             "  --member-role demandspike   #{data " + jsonJOB + "}#",
    onResponse: function(resp) {
      new cluster.ResponsePrinter(console, resp).print();
      Assert.assertTrue(resp.success && !resp.isEmpty()) ;
    }
  }) ;
} ;

var helloJob = {
  "id":   "demandspike-hello-job",
  "description": "Sample DemandSpike job",

  "tasks": [
    {
      "description": "clean metric task",
      "command":     "server metric-clear --expression *"
    },
    {
      "description": "send by the dummy driver",
      "command":     "demandspike:job send --max-num-of-message 1000"
    },
    {
      "description": "send by the kafka driver",
      "command":  "demandspike:job send " + 
                  "  --driver kafka --broker-connect 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094" + 
                  "  --topic metrics.consumer --max-num-of-message 5000"
    }
  ]
}

submitDemandSpikeJob(helloJob) ;

function submitKafkaLoadTestWithDifferentSize() {
  for(var i = 0; i < 3; i++) {
    var size = 1024 * (i + 1) ;
    var kafkaLoadTestJob = {
      "id":   "demandspike-kafka-load-test-message-size-" + size ,
      "description": "Load test kafka server with message size " + size,

      "tasks": [
        {
          "description": "clean metric task",
          "command":     "server metric-clear --expression *"
        },
        {
          "description": "send by the kafka driver",
          "command":  "demandspike:job send " + 
                      "  --driver kafka --broker-connect 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094" + 
                      "  --topic metrics.consumer --max-num-of-message 5000000 --max-duration 300000" +
                      "  --message-size " + size
        }
      ]
    }
    submitDemandSpikeJob(kafkaLoadTestJob) ;
  }
}

submitKafkaLoadTestWithDifferentSize() ;


function submitKafkaLoadTestWithDifferentAck() {
  for(var i = 0; i < 2; i++) {
    var kafkaLoadTestJob = {
      "id":   "demandspike-kafka-load-test-acks=" + i ,
      "description": "Load test kafka server with request.required.acks=" + i,

      "tasks": [
        {
          "description": "clean metric task",
          "command":     "server metric-clear --expression *"
        },
        {
          "description": "send by the kafka driver",
          "command":  "demandspike:job send " + 
                      "  --driver kafka  --driver:request.required.acks=" + i +
                      "  --broker-connect 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094" + 
                      "  --topic metrics.consumer --max-num-of-message 5000000 --max-duration 300000" +
                      "  --message-size 1024"
        }
      ]
    }
    submitDemandSpikeJob(kafkaLoadTestJob) ;
  }
}

submitKafkaLoadTestWithDifferentAck() ;
