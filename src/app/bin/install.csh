:echo "List the current server status"
:echo "==============================="
server ping

:echo "list the server and services registration"
:echo "=======================================--"
server registration

:echo "Install zookeeper on the zookeeper server role"
:echo "==============================================="
module install  \
  -Pmodule.data.drop=true \
  -Pzk:clientPort=2181 \
  --member-role zookeeper --autostart --module Zookeeper

:sleep 1000
:echo "Install kafka on the kafka server role"
:echo "========================================="
module install \
  -Pmodule.data.drop=true \
  -Pkafka:port=9092 -Pkafka:zookeeper.connect=127.0.0.1:2181 \
  -Pkafka.zookeeper-urls=127.0.0.1:2181 \
  --member-role kafka --autostart --module Kafka

:echo "Install Sparkngin on the sparkngin server role"
:echo "=============================================="
module install  --member-role sparkngin --autostart --module Sparkngin 
