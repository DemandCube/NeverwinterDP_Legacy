#!/bin/bash

cygwin=false
case "`uname`" in
  CYGWIN*) cygwin=true;;
esac

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`
APP_DIR=`cd $bin/..; pwd; cd $bin`
JAVACMD=$JAVA_HOME/bin/java

if $cygwin; then
  APP_DIR=`cygpath --absolute --windows "$APP_DIR"`
fi

JAVA_OPTS="-Xshare:auto -Xms128m -Xmx1536m -XX:-UseSplitVerifier" 
APP_OPT="-Dapp.dir=$APP_DIR -Duser.dir=$APP_DIR"
LOG_OPT="-Dlog4j.configuration=file:$APP_DIR/config/single-jvm-log4j.properties"

MAIN_CLASS="com.neverwinterdp.server.MultiServer"

function startServer {
  nohup $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $APP_OPT $LOG_OPT $MAIN_CLASS "$@" <&- &>/dev/null &
  #printf '%d' $! > $SERVER_NAME.pid
}

function runServer {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $JAVA_OPTS $APP_OPT $LOG_OPT $MAIN_CLASS "$@"
}

startServer \
  -Pgeneric:server.name=generic -Pgeneric:server.roles=generic  \
  -Pelasticsearch1:server.name=elasticsearch1 -Pelasticsearch1:server.roles=elasticsearch  \
  -Pelasticsearch2:server.name=elasticsearch2 -Pelasticsearch2:server.roles=elasticsearch  \
  -Pzookeeper1:server.name=zookeeper1 -Pzookeeper1:server.roles=zookeeper \
  -Pkafka1:server.name=kafka1 -Pkafka1:server.roles=kafka \
  -Pkafka2:server.name=kafka2 -Pkafka2:server.roles=kafka \
  -Pkafka3:server.name=kafka3 -Pkafka3:server.roles=kafka \
  -Psparkngin1:server.name=sparkngin1 -Psparkngin1:server.roles=sparkngin \
  -Pdemandspike1:server.name=demandspike1 -Pdemandspike1:server.roles=demandspike
