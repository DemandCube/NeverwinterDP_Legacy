ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");

if(typeof MAX_DURATION == 'undefined') MAX_DURATION = 60000 ;
if(typeof SPARKNGIN_BROKER == 'undefined') SPARKNGIN_BROKER = "127.0.0.1:7080" ;

SHELL.exec(":echo MAX_DURATION     = " + MAX_DURATION) ;
SHELL.exec(":echo SPARKNGIN_BROKER = " + SPARKNGIN_BROKER) ;

SHELL.exec("server metric-clear --expression *");


SHELL.exec("service stop    --member-role generic   --module KafkaConsumer --service-id KafkaMetricsConsumerService --timeout 60000");
SHELL.exec("service stop    --member-role sparkngin --module Sparkngin     --service-id SparknginClusterHttpService --timeout 60000");
SHELL.exec("service stop    --member-role kafka     --module Kafka         --service-id KafkaClusterService         --timeout 60000");
SHELL.exec("service stop    --member-role zookeeper --module Zookeeper     --service-id ZookeeperClusterService     --timeout 60000");

//SHELL.exec("service cleanup --member-role kafka     --module Kafka         --service-id KafkaClusterService         --timeout 60000");
//SHELL.exec("service cleanup --member-role sparkngin --module Sparkngin     --service-id SparknginClusterHttpService --timeout 60000");

SHELL.exec("service start   --member-role zookeeper --module Zookeeper     --service-id ZookeeperClusterService     --cleanup  --timeout 60000");
SHELL.exec("service start   --member-role kafka     --module Kafka         --service-id KafkaClusterService         --cleanup  --timeout 60000");
SHELL.exec("service start   --member-role generic   --module KafkaConsumer --service-id KafkaMetricsConsumerService --cleanup  --timeout 60000");
SHELL.exec("service start   --member-role sparkngin --module Sparkngin     --service-id SparknginClusterHttpService --cleanup  --timeout 60000");

SHELL.exec(
  "ringbearer:job send " + 
  "  --driver sparkngin --broker-connect " + SPARKNGIN_BROKER +  
  "  --topic metrics.consumer --max-num-of-message 5000000 --max-duration " + MAX_DURATION 
) ;

SHELL.exec(":echo Server metrics") ;
SHELL.exec(":echo *****************************************************************************************") ;
SHELL.exec( "server metric") ;
