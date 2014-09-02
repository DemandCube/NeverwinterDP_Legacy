#!/bin/bash

cygwin=false
ismac=false
case "`uname`" in
  CYGWIN*) cygwin=true;;
  Darwin) ismac=true;;
esac

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`
APP_DIR=`cd $bin/..; pwd; cd $bin`
JAVACMD=$JAVA_HOME/bin/java

if [ "x$JAVA_HOME" == "x" ] ; then 
  echo "WARNING JAVA_HOME is not set"
fi

(which $JAVACMD)
isjava=$?

if $ismac && [ $isjava -ne 0 ] ; then
  which java
  if [ $? -eq 0 ] ; then
    JAVACMD=`which java`
    echo "Defaulting to java: $JAVACMD"
  else 
    echo "JAVA Command (java) Not Found Exiting"
    exit 1
  fi
fi

if $cygwin; then
  APP_DIR=`cygpath --absolute --windows "$APP_DIR"`
fi

#Yourkit Profiler
YOURKIT_LIB="/Users/Tuan/Tools/YourKit-2013.app/bin/mac/libyjpagent.jnilib"
#YOURKIT_OPTS="-agentpath:$YOURKIT_LIB=disablestacktelemetry,disableexceptiontelemetry,builtinprobes=none,delay=10000,sessionname=NeverwinterDP"

JAVA_OPTS="-Xshare:auto -Xms128m -Xmx192m -XX:-UseSplitVerifier" 
APP_OPT="-Dapp.dir=$APP_DIR -Duser.dir=$APP_DIR"
LOG_OPT="-Dlog4j.configuration=file:$APP_DIR/config/log4j.properties"

MAIN_CLASS="com.neverwinterdp.server.Server"

function startServer {
  nohup $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $APP_OPT $YOURKIT_OPTS $LOG_OPT $MAIN_CLASS "$@" <&- &>/dev/null &
  #printf '%d' $! > $SERVER_NAME.pid
}

function runServer {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $JAVA_OPTS $APP_OPT $LOG_OPT $MAIN_CLASS "$@"
}

echo "LOG_OPTS = $LOG_OPT"

#runServer -Pserver.name=generic -Pserver.roles=generic

startServer -Pserver.name=generic -Pserver.roles=generic

startServer -Pserver.name=elasticsearch1 -Pserver.roles=elasticsearch
startServer -Pserver.name=elasticsearch2 -Pserver.roles=elasticsearch

startServer -Pserver.name=zookeeper1 -Pserver.roles=zookeeper

startServer -Pserver.name=kafka1 -Pserver.roles=kafka
startServer -Pserver.name=kafka2 -Pserver.roles=kafka
startServer -Pserver.name=kafka3 -Pserver.roles=kafka

startServer -Pserver.name=sparkngin1 -Pserver.roles=sparkngin

startServer -Pserver.name=ringbearer1 -Pserver.roles=ringbearer
