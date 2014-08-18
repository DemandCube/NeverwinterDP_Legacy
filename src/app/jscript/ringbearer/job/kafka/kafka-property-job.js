ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");

if(typeof MAX_DURATION == 'undefined') MAX_DURATION = 60000 ;
if(typeof KAFKA_BROKER == 'undefined') KAFKA_BROKER = "127.0.0.1:9092" ;
if(typeof MESSAGE_SIZE == 'undefined') MESSAGE_SIZE = 1024 ;
if(typeof PROPERTY != 'undefined' && PROPERTY != "") DRIVER_PROPERTY = " --driver:"+PROPERTY; else DRIVER_PROPERTY="";

SHELL.exec(":echo MAX_DURATION = " + MAX_DURATION) ;
SHELL.exec(":echo KAFKA_BROKER = " + KAFKA_BROKER) ;
SHELL.exec(":echo MESSAGE_SIZE = " + MESSAGE_SIZE);
SHELL.exec(":echo PROPERTY = " + PROPERTY);

SHELL.exec("service stop    --member-role generic --module KafkaConsumer --service-id KafkaMetricsConsumerService --timeout 60000");
SHELL.exec("service stop    --member-role kafka   --module Kafka         --service-id KafkaClusterService         --timeout 60000");

SHELL.exec("service cleanup --member-role kafka   --module Kafka         --service-id KafkaClusterService         --timeout 60000");

SHELL.exec("service start   --member-role kafka   --module Kafka         --service-id KafkaClusterService         --timeout 60000");
SHELL.exec("service start   --member-role generic --module KafkaConsumer --service-id KafkaMetricsConsumerService --timeout 60000");

SHELL.exec(
  "ringbearer:job send " + 
  "  --driver kafka "+DRIVER_PROPERTY+" --broker-connect " + KAFKA_BROKER + 
  "  --topic metrics.consumer  --max-duration " + MAX_DURATION +
  "  --message-size " + MESSAGE_SIZE
);

SHELL.exec("server metric");
