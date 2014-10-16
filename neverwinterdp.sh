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

function printHeader() {
	echo ""
	echo "****************************************************************************************"
	echo ""
	echo "Project: $1"
	echo ""
	echo "****************************************************************************************"
	echo ""
	echo ""
}

function runGradle() {
  COMMAND=$1
  shift
  
  printHeader "NeverwinterDP-Commons"
  echo "NeverwinterDP-Commons: $COMMAND $@" 
  cd $APP_DIR/NeverwinterDP-Commons && $COMMAND $@
  
  printHeader "Queuengin"
  echo "Queuengin: $COMMAND $@"
  cd $APP_DIR/Queuengin && $COMMAND $@
  
  printHeader "Sparkngin"
  echo "Sparkngin: $COMMAND $@"
  cd $APP_DIR/Sparkngin && $COMMAND $@
  
  printHeader "Scribengin"
  echo "Scribengin: $COMMAND $@"
  cd $APP_DIR/Scribengin && $COMMAND $@
  
  printHeader "DemandSpike"
  echo "DemandSpike: $COMMAND $@"
  cd $APP_DIR/DemandSpike && $COMMAND $@
  
  printHeader "NeverwinterDP"
  echo "NeverwinterDP: $COMMAND $@"
  cd $APP_DIR/NeverwinterDP && $COMMAND $@
  cd $APP_DIR
  echo $@
}



function runGit() { 
  
  printHeader "NeverwinterDP-Commons"
  echo "NeverwinterDP-Commons: git $@"
  cd $APP_DIR/NeverwinterDP-Commons && git $@
  
  printHeader "Queuengin"
  echo "Queuengin: git $@"
  cd $APP_DIR/Queuengin && git $@
  
  printHeader "Sparkngin"
  echo "Sparkngin: git $@"
  cd $APP_DIR/Sparkngin && git $@
  
  printHeader "Scribengin"
  echo "Scribengin: git $@"
  cd $APP_DIR/Scribengin && git $@
  
  printHeader "DemandSpike"
  echo "DemandSpike: git $@"
  cd $APP_DIR/DemandSpike && git $@
  
  printHeader "NeverwinterDP"
  echo "NeverwinterDP: git $@"
  cd $APP_DIR/NeverwinterDP && git $@
  cd $APP_DIR
}


function release() {
  echo "do release"
}

function runCheckout() { 
  
  printHeader "NeverwinterDP-Commons"
  echo "NeverwinterDP-Commons: git clone https://github.com/DemandCube/NeverwinterDP-Commons.git"
  if [ ! -d $APP_DIR/NeverwinterDP-Commons ] ; then
    cd $APP_DIR && git clone https://github.com/DemandCube/NeverwinterDP-Commons.git
  fi
  
  printHeader "Queuengin"
  echo "Queuengin: git clone https://github.com/DemandCube/Queuengin.git"
  if [ ! -d $APP_DIR/Queuengin ] ; then
    cd $APP_DIR && git clone https://github.com/DemandCube/Queuengin.git
  fi
  
  printHeader "Sparkngin"
  echo "Sparkngin: git clone https://github.com/DemandCube/Sparkngin.git"
  if [ ! -d $APP_DIR/Sparkngin ] ; then
    cd $APP_DIR && git clone https://github.com/DemandCube/Sparkngin.git
  fi
  
  printHeader "Scribengin"
  echo "Scribengin: git clone https://github.com/DemandCube/Scribengin.git"
  if [ ! -d $APP_DIR/Scribengin ] ; then
    cd $APP_DIR && git clone https://github.com/DemandCube/Scribengin.git
  fi
  
  printHeader "DemandSpike"
  echo "DemandSpike: git clone https://github.com/DemandCube/DemandSpike.git"
  if [ ! -d $APP_DIR/DemandSpike ] ; then
    cd $APP_DIR && git clone https://github.com/DemandCube/DemandSpike.git
  fi
}


COMMAND=$1
shift

if [ "$COMMAND" = "gradle" ] ; then
  runGradle $COMMAND $@
elif [ "$COMMAND" = "gradlew"  ] ; then
  runGradle "./gradlew" $@
elif [ "$COMMAND" = "git" ] ; then
  runGit $@
elif [ "$COMMAND" = "release" ] ; then
  release $@
elif [ "$COMMAND" = "checkout" ] ; then
  runCheckout
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
  echo "  gradle: Run gradle against all projects"
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
  echo "	./neverwinterdp.sh checkout"
  echo ""
  echo "Will clone the other repos NeverwinterDP-Commons, Queuengin, Sparkngin, Scribengin, DemandSpike"
  echo ""
  echo ""
fi
