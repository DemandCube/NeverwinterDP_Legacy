ScriptRunner.require("classpath:util/io.js");

if(typeof MAX_DURATION == 'undefined') MAX_DURATION = 60000 ;
if(typeof KAFKA_BROKER == 'undefined') KAFKA_BROKER = "kafka-1:9092,kafka-2:9092" ;

SHELL.exec(":echo MAX_DURATION = " + MAX_DURATION) ;
SHELL.exec(":echo KAFKA_BROKER = " + KAFKA_BROKER) ;

SHELL.exec("server metric-clear --expression *");
SHELL.exec("ringbearer:job send --max-num-of-message 1000") ;
SHELL.exec(
  "ringbearer:job simulation " +
  "  --name service-failure --target-member-role kafka --module Kafka --service-id KafkaClusterService " +
  "  --delay 3000 --period 5000 --failure-time 1000"
) ;
SHELL.exec(
  "ringbearer:job send " + 
  "  --driver kafka --broker-connect " + KAFKA_BROKER + 
  "  --topic metrics.consumer --max-num-of-message 1000"
) ;
