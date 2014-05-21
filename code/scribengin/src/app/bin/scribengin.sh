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

function runScribengin() {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs com.neverwinterdp.scribengin.Main "$@"
}


COMMAND=$1
shift

if [ "$COMMAND" = "run" ] ; then
  runScribengin "$@"
else
  echo "Avaliable Commands: "
  echo "  run: Run scribengin"
fi
