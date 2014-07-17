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

JAVA_OPTS="-Xshare:auto -Xms128m -Xmx256m -XX:-UseSplitVerifier" 
APP_OPT="-Dapp.dir=$APP_DIR -Duser.dir=$APP_DIR"
LOG_OPT="-Dlog4j.configuration=file:$APP_DIR/config/log4j.properties"

MAIN_CLASS="com.neverwinterdp.hadoop.yarn.app.AppClient"

function runClient {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $JAVA_OPTS $APP_OPT $LOG_OPT $MAIN_CLASS "$@"
}


#runServer -Pserver.name=zookeeper -Pserver.roles=zookeeper

runClient \
   --app-home /tmp/app/DemmandSpike \
   --upload-app $APP_DIR \
   --app-name "NeverwinterDP DemandSpike App" \
   --container-manager com.neverwinterdp.demandspike.yarn.AsyncDemandSpikeAppContainerManager \
   --conf:fs.default.name=hdfs://hadoop:9000 \
   --conf:dfs.replication=1 \
   --conf:yarn.resourcemanager.scheduler.address=hadoop:8030 \
   --conf:yarn.resourcemanager.address=hadoop:8032 \
   --conf:demandspike.instance.core=1 \
   --conf:demandspike.instance.memory=128 \
   --conf:demandspike.job.num-of-task=10 \
   --conf:demandspike.job.driver=kafka \
   --conf:demandspike.job.topic=metrics.consumer \
   --conf:demandspike.job.connect-url=192.168.1.100:9092 \
   --conf:demandspike.job.max-duration=15000
