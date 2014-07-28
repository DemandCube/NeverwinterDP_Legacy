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

function runGradle() { 
  cd $APP_DIR/NeverwinterDP-Commons && gradle $@
  cd $APP_DIR/Queuengin && gradle $@
  cd $APP_DIR/Sparkngin && gradle $@
  cd $APP_DIR/Scribengin && gradle $@
  cd $APP_DIR/DemandSpike && gradle $@
  cd $APP_DIR
}

function runGit() { 
  cd $APP_DIR/NeverwinterDP-Commons && git $@
  cd $APP_DIR/Queuengin && git $@
  cd $APP_DIR/Sparkngin && git $@
  cd $APP_DIR/Scribengin && git $@
  cd $APP_DIR/DemandSpike && git $@
  cd $APP_DIR
}


function release() {
  echo "do release"
}


COMMAND=$1
shift

if [ "$COMMAND" = "gradle" ] ; then
  runGradle $@
elif [ "$COMMAND" = "git" ] ; then
  runGit $@
elif [ "$COMMAND" = "release" ] ; then
  release $@
else
  echo "Avaliable Commands: "
  echo "  build: Run gradle clean build in all the projects"
  echo "  release: Run gradle release in Queuengin, Sparkngin, Scribengin"
fi
