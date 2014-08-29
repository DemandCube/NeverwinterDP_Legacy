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

PID_FILE="$APP_DIR/bin/kafka.pid"
APP_OPT="-Dapp.dir=$APP_DIR -Duser.dir=$APP_DIR"

MAIN_CLASS="com.neverwinterdp.server.shell.Shell"

function runShell {
  $JAVACMD -Djava.ext.dirs=$APP_DIR/libs $APP_OPT $MAIN_CLASS "$@"
}

echo "runShell $@"
runShell "$@"
