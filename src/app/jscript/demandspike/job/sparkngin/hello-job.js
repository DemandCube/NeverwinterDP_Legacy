ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");

if(typeof MAX_DURATION == 'undefined') MAX_DURATION = 60000 ;
if(typeof SPARKNGIN_BROKER == 'undefined') SPARKNGIN_BROKER = "127.0.0.1:7080" ;

SHELL.exec(":echo MAX_DURATION     = " + MAX_DURATION) ;
SHELL.exec(":echo SPARKNGIN_BROKER = " + SPARKNGIN_BROKER) ;

SHELL.exec("server metric-clear --expression *");
SHELL.exec("demandspike:job send --max-num-of-message 1000") ;

SHELL.exec(
  "demandspike:job send " + 
  "  --driver sparkngin --broker-connect " + SPARKNGIN_BROKER +  
  "  --topic metrics.consumer --max-num-of-message 1000"
) ;

SHELL.exec(":echo Server metrics") ;
SHELL.exec(":echo *****************************************************************************************") ;
SHELL.exec( "server metric") ;
