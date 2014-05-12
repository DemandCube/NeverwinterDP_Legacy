#!/bin/bash

cygwin=false
case "`uname`" in
  CYGWIN*) cygwin=true;;
esac

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`
APP_DIR=`cd $bin/..; pwd; cd $bin`

if $cygwin; then
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  APP_DIR=`cygpath --absolute --windows "$APP_DIR"`
fi

export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:$APP_DIR/config/tools-log4j.properties"

TOPICCMD="./kafka-topics.sh" 

function topicList() {
  $TOPICCMD --list --zookeeper localhost:2181
}

function topicCreate() {
  topicName=$1
  $TOPICCMD --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic $topicName
}

function topicProduce() {
  topicName=$1
  ./kafka-console-producer.sh --broker-list 127.0.0.1:9092 --request-required-acks 1 --topic $topicName
}

function topicConsume() {
  topicName=$1
  ./kafka-console-consumer.sh --zookeeper 127.0.0.1:2181 --from-beginning --topic $topicName
}


COMMAND=$1
shift

if [ "$COMMAND" = "topicList" ] ; then
  topicList
elif [ "$COMMAND" = "topicCreate" ] ; then
  topicCreate $1
elif [ "$COMMAND" = "topicProduce" ] ; then
  topicProduce $1
elif [ "$COMMAND" = "topicConsume" ] ; then
  topicConsume $1
else
  echo "Avaliable Commands: "
  echo "  topicList:  List the available topics "
  echo "  topicCreate:  create a topic "
fi
