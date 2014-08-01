ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");

SHELL.exec("server metric-clear --expression *");
SHELL.exec("ringbearer:job send --max-num-of-message 1000")
SHELL.exec(
  "ringbearer:job simulation " + 
  "  --name service-failure --target-member-role kafka --module Kafka --service-id KafkaClusterService " +
  "  --delay 3000 --period 5000 --failure-time 1000"
) ;
SHELL.exec("ringbearer:job send --driver kafka --broker-connect 127.0.0.1:9092 --topic metrics.consumer --max-num-of-message 1000") ;
