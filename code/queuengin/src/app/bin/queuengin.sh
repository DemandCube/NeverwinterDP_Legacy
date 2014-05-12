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
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  APP_DIR=`cygpath --absolute --windows "$APP_DIR"`
fi

function helloQueuengin() {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs com.neverwinterdp.queuengin.HelloQueuengin "$@"
}


COMMAND=$1
shift

if [ "$COMMAND" = "hello" ] ; then
  helloQueuengin "$@"
else
  echo "Avaliable Commands: "
  echo "  hello: Run hello producer and consumer"
fi
