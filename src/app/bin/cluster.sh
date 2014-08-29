#!/usr/bin/env bash

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

USER="neverwinterdp"
APP_HOME=/opt/NeverwinterDP

GENERIC_SERVERS="";
ZOOKEEPER_SERVERS="";
KAFKA_SERVERS="";
SPARKNGIN_SERVERS="";
ALL_SERVERS="";

#Parse /etc/hosts file to get the cluster hostname
function parseHostsFile() {
  FILENAME="/etc/hosts"
  while read LINE
  do
    if [[ $LINE ==  $'## vagrant-hostmanager-start'* ]] 
    then  
     BEGIN_VAGRANT=TRUE 
     continue
    elif [[ $LINE == \#* ]]   #Ignore the comment line
    then  
      continue 
    elif [[ $LINE == "" ]]    #Ignore the empty line
    then  
      continue 
    fi
    
    if [[ -n "$BEGIN_VAGRANT" ]] #Check not null, empty BEGIN_VAGRANT
    then
      arrLine=(${LINE// /}) #split line that contains '$IP $HOSTNAME' format
      hostname=${arrLine[1]}

      if [[ $hostname ==  $'generic'* ]] 
      then  
       GENERIC_SERVERS="$GENERIC_SERVERS $hostname"
      elif [[ $hostname ==  $'zookeeper'* ]] 
      then  
       ZOOKEEPER_SERVERS="$ZOOKEEPER_SERVERS $hostname"
      elif [[ $hostname ==  $'kafka'* ]] 
      then  
       KAFKA_SERVERS="$KAFKA_SERVERS $hostname"
      elif [[ $hostname ==  $'sparkngin'* ]] 
      then  
       SPARKNGIN_SERVERS="$SPARKNGIN_SERVERS $hostname"
      fi
    fi
  done < $FILENAME
  
  ALL_SERVERS="$GENERIC_SERVERS$ZOOKEEPER_SERVERS $KAFKA_SERVERS $SPARKNGIN_SERVERS"

}

function printHeader() {
  echo ""
  echo "###########################################################################################################"
  echo "$@"
  echo "###########################################################################################################"
}

function confirmYN() {
 while true; do
    read -p "$@" yn
    case $yn in
      [Yy]* ) break;;
      [Nn]* ) exit;;
      * ) echo "Please answer yes or no.";;
    esac
  done
}

function cluster_exec() {
  for server in $ALL_SERVERS; do
    printHeader "Execute '$@' On  $server"
    ssh $USER@$server "cd $bin && $@"
  done
}

function cluster_sync() {
  for server in $ALL_SERVERS; do
    printHeader "synchronized data with $server"
    rsync -r -c -P --delete $APP_HOME $USER@$server:/opt/
  done
}

function start_min_cluster() {
  for server in $ALL_SERVERS; do
    printHeader "Execute '$@' On  $server"
    if [ "$server" = "generic" ] ; then
      ssh $USER@$server "export JAVA_HOME='/usr/java/default' && $APP_HOME/bin/service.sh -Pserver.name=generic -Pserver.roles=generic,elasticsearch,ringbearer"
    else
      ssh $USER@$server "export JAVA_HOME='/usr/java/default' && $APP_HOME/bin/service.sh"
    fi
  done
}

function cluster_taillog() {
  LINE=$1
  if [ "$LINE" == "" ] ; then
    LINE="20"
  fi
  cluster_exec "find  $APP_HOME/logs -name "*.log" -exec tail -n 20  {} \; -print"
}

parseHostsFile 

# get sub command
COMMAND=$1
shift


echo ""
echo "************************************************************************************************************"
echo "Server generic role:   $GENERIC_SERVERS"
echo "Server zookeeper role: $ZOOKEEPER_SERVERS"
echo "Server kafka role:     $KAFKA_SERVERS"
echo "Server sparkngin role: $SPARKNGIN_SERVERS"
echo "All Server::           $ALL_SERVERS"
echo "************************************************************************************************************"
echo ""

if [ "$COMMAND" = "exec" ] ; then
  cluster_exec $@
elif [ "$COMMAND" = "sync" ] ; then
  confirmYN "Do you want to sync this program with the other members(Y/N)?"
  cluster_sync
elif [ "$COMMAND" = "start-min-cluster" ] ; then
  confirmYN "Did you make sure that all the NeverwinterDP processes are not running (Y/N)?"
  start_min_cluster
elif [ "$COMMAND" = "clean-start-min-cluster" ] ; then
  confirmYN "Do you want to kill all the running processes, clean data, log and start (Y/N)?"
  cluster_exec "pkill -9 java && ps -x | grep NeverwinterDP"
  cluster_exec "rm -rf /opt/NeverwinterDP/logs /opt/NeverwinterDP/data"
  cluster_sync
  start_min_cluster
elif [ "$COMMAND" = "clean-log" ] ; then
  confirmYN "Do you really want to remove the log(Y/N)?"
  cluster_exec "rm -rf /opt/NeverwinterDP/logs"
elif [ "$COMMAND" = "clean-all" ] ; then
  confirmYN "Do you really want to remove all the log and data(Y/N)?"
  cluster_exec "rm -rf /opt/NeverwinterDP/logs /opt/NeverwinterDP/data"
elif [ "$COMMAND" = "taillog" ] ; then
  LINE=$1
  if [ "$LINE" == "" ] ; then
    LINE="20"
  fi
  cluster_exec "find  $APP_HOME/logs -name "*.log" -exec tail -n 20  {} \; -print"
elif [ "$COMMAND" = "loggrep" ] ; then
  cluster_exec "find  $APP_HOME/logs -name "*.log" -exec grep $@  {} \; -print"
elif [ "$COMMAND" = "kill" ] ; then
  confirmYN "Do you really want to kill all NeverwinterDP processes(Y/N)?"
  cluster_exec "pkill -9 java && ps -x | grep NeverwinterDP"
elif [ "$COMMAND" = "status" ] ; then
  cluster_exec "ps -x | grep NeverwinterDP"
else
  echo "cluster command options: "
  echo "  start-min-cluster     : To start a minimum neverwinterdp cluster cluster "
  echo "  clean-all             : To remove the data , logs and pid files"
  echo "  clean-log             : To remove logs and pid files"
  echo "  taillog n             : To view last n lines from log files. Default is 30"
  echo "  greplog               : To search log"
  echo "  kill                  : To kill all NeverwinterDP processes"
  echo "  status                : To list the NeverwinterDP processes"
  echo "  exception             : To view exception from log files"
  echo "  exec                  : To execute the shell command on all the servers or a group of servers"
  echo "  sync                  : To copy this program to the fetcher members"
fi
