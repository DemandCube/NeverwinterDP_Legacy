#!/usr/bin/env bash


function printHeader() {
	echo "****************************************************************************************"
	echo "$1"
	echo "****************************************************************************************"
}

printHeader "Install JDK 1.7"

sudo rpm -ivh /vagrant/apps/rpm/jdk-7u67-linux-x64.rpm

sudo echo "" >> /etc/profile
sudo echo "JAVA_HOME=/usr/java/default" >> /etc/profile
sudo echo "export JAVA_HOME" >> /etc/profile
export JAVA_HOME="/usr/java/default"

printHeader "Create neverwinterdp user account and setup home directory"
sudo useradd -m -d /home/neverwinterdp -s /bin/bash -c "neverwinterdp user" -p $(openssl passwd -1 neverwinterdp)  neverwinterdp

#Copy neverinterdp home template
sudo cp -R  /vagrant/home/neverwinterdp/.[a-zA-Z0-9]* /home/neverwinterdp
sudo chown -R neverwinterdp /home/neverwinterdp
sudo chgrp -R neverwinterdp /home/neverwinterdp

printHeader "Install NeverwinterDP to /opt"
sudo mkdir /opt/NeverwinterDP
sudo tar -zxf  /vagrant/apps/java/neverwinterdp-*.tgz -C /opt/NeverwinterDP
sudo chown -R neverwinterdp /opt/NeverwinterDP
sudo chgrp -R neverwinterdp /opt/NeverwinterDP

printHeader "Install Yourkit to /opt"
sudo cp -R  /vagrant/apps/java/Yourkit-2014 /opt
sudo chown -R neverwinterdp /opt/Yourkit-2014
sudo chgrp -R neverwinterdp /opt/Yourkit-2014
