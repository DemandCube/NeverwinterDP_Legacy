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
  echo "NeverwinterDP-Commons: gradle $@" 
  cd $APP_DIR/NeverwinterDP-Commons && gradle $@
  echo "Queuengin: gradle $@"
  cd $APP_DIR/Queuengin && gradle $@
  echo "Sparkngin: gradle $@"
  cd $APP_DIR/Sparkngin && gradle $@
  echo "Scribengin: gradle $@"
  cd $APP_DIR/Scribengin && gradle $@
  echo "DemandSpike: gradle $@"
  cd $APP_DIR/DemandSpike && gradle $@
  echo "NeverwinterDP: gradle $@"
  cd $APP_DIR/NeverwinterDP && gradle $@
  cd $APP_DIR
}

function runGit() { 
  echo "NeverwinterDP-Commons: git $@"
  cd $APP_DIR/NeverwinterDP-Commons && git $@
  echo "Queuengin: git $@"
  cd $APP_DIR/Queuengin && git $@
  echo "Sparkngin: git $@"
  cd $APP_DIR/Sparkngin && git $@
  echo "Scribengin: git $@"
  cd $APP_DIR/Scribengin && git $@
  echo "DemandSpike: git $@"
  cd $APP_DIR/DemandSpike && git $@
  echo "NeverwinterDP: git $@"
  cd $APP_DIR/NeverwinterDP && git $@
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
  echo "This command will run git or gradle against all projects."
  echo "Projects:"
  echo "	NeverwinterDP-Commons"
  echo "	Queuengin"
  echo "	Sparkngin"
  echo "	Scribengin"
  echo "	DemandSpike"
  echo "	NeverwinterDP"
  echo ""
  echo "Avaliable Commands: "
  echo "  build: Run gradle against all projects"
  echo "  git: Run git against all projects"
  echo ""
  echo "e.g."
  echo "cd NeverwinterDP"
  echo "	./neverwinterdp.sh  git [pass through parameters]"
  echo "	./neverwinterdp.sh  gradle [pass through parameters]"
  echo ""
  echo "For example the command"
  echo "	./neverwinterdp.sh git pull origin"
  echo ""
  echo "Will pull or the latest code from  NeverwinterDP-Commons, Queuengin, Sparkngin, Scribengin, DemandSpike, NeverwinterDP"
  echo ""
  echo ""
  echo "	./neverwinterdp.sh gradle clean build install"
  echo ""
  echo "Will clean and build code in NeverwinterDP-Commons, Queuengin, Sparkngin, Scribengin, DemandSpike, NeverwinterDP"
  echo ""
  echo ""
fi
