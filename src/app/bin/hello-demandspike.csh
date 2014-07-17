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

:echo "Install DemandSpike on the demandspike server role"
:echo "=================================================="
module install  --member-role demandspike --autostart --module DemandSpike

:echo "list the server and services registration"
:echo "=========================================="
server registration

:echo "Submit a demandspike job"
:echo "=========================================="
demandspike submit  \
  --driver kafka --broker-connect 127.0.0.1:9092 --topic metrics.consumer \
  --num-of-task 2  --num-of-thread 2 --message-size 2048 \
  --member-role demandspike --max-duration 60000 --max-num-of-message 500000

:echo "Wait for 60s................."
:sleep 60000


:echo "Show the timer metrics"
:echo "====================="
server metric --type timer

:echo "Uninstall DemandSpike server on the demandspike role servers"
:echo "============================================================"
module uninstall --member-role demandspike --timeout 20000 --module DemandSpike 

:echo "Uninstall Sparkngin server on the sparkngin role servers"
:echo "============================================================"
module uninstall --member-role sparkngin --timeout 20000 --module Sparkngin 

:echo "Uninstall Kafka server on the kafka role servers"
:echo "================================================"
module uninstall --member-role kafka --timeout 20000 --module Kafka

:echo "Uninstall Zookeeper server on the zookeeper role servers"
:echo "========================================================"
module uninstall --member-role zookeeper --timeout 20000 --module Zookeeper

:echo "list the server and services registration after uninstall kafka and zookeeper service"
:echo "====================================================================================="
server registration


:echo "shutdown the cluster"
:echo "===================="
server exit
