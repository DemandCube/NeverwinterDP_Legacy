#!/bin/bash

function usage
{
    echo "Usage:"
    echo "  initvm            :Launch multiple VM"
    echo "  setupvm           :Installs NeverwinterDP in all VM and launch nervinterdp services"
    echo "  status            :Shows VM status"
    echo "  build             :Checkin latest NeverwinterDP projects and build"
    echo "  clean             :Destroy VM and clean files"
    echo "  install           :Launch multiple VM, build NeverwinterDP, Installs NeverwinterDP and run neverwinterDP service"
    echo "  -p | --provider   :Virtual Machine provider (virtualbox|digital_ocean)"
    echo "  -r | --release    :Release path of NeverwinterDP (eg: /Users/username/NeverwinterDP/build/release)"
}

if [ "$1" == "" ]; then
    usage
    exit 1
fi

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

HOME_DIR=$bin
WORKING_DIR=$HOME_DIR/tmp

set -e

function printHeader() {
	echo ""
	echo "****************************************************************************************"
	echo "$1"
	echo "****************************************************************************************"
	echo ""
}

function initvm() {
  clean
  printHeader "Init NeverwinterDP Cluster VM Machines"


if [ ! -d "$WORKING_DIR/DemandCubePlaybooks" ]; then
    installDemandCubePlaybooks
fi




  echo "vagrant plugin install vagrant-flow"
  vagrant plugin install vagrant-flow

  echo "vagrant flow multiinit"
  #It seems that vagrant flow multiinit return error, need to disable exit on error
  set +o errexit
  vagrant flow multiinit
  set -o errexit

  echo "vagrant up --provider=$VM_PROVIDER" 
  vagrant up --provider=$VM_PROVIDER
}

function installDemandCubePlaybooks() {
    printHeader "Install DemandCubePlaybooks"
    rm -rf $WORKING_DIR/DemandCubePlaybooks
    mkdir -p $WORKING_DIR
    cd $WORKING_DIR
    echo "Downloading DemandCubePlaybooks"
    git clone https://github.com/jeroldleslie/DemandCubePlaybooks.git
    echo "Downloading ansible-flow library"
    cd DemandCubePlaybooks
    git clone https://github.com/DemandCube/ansible-flow.git
    mv -f  ./ansible-flow/library/ ./library
    rm -rf ./ansible-flow
    cd $HOME_DIR
}

function buildProject() {

    if [ "$RELEASE_PATH" != "" ]; then
        if [ ! -d "$RELEASE_PATH/NeverwinterDP" ]; then
            buildConfirmation
        fi
    else
        echo "No release path is found."
        buildConfirmation
    fi
}

function buildConfirmation() {
    while true; do
        read -p "No release found, NeverwinterDP release is required to continue. Do you wish to build latest NeverwinterDP [y/n]?" yn
        case $yn in
            [Yy]* )
                echo "It may take time to build release NeverwinterDP.";
                buildNeverwinterDP;

                break;;
            [Nn]* ) exit;;
            * ) echo "Please answer y or n.";;
        esac
    done
}

function buildNeverwinterDP() {
    printHeader "Build The NeverwinterDP  Project"


    CODE_DIR=$WORKING_DIR/build/code
    rm -rf $CODE_DIR
    mkdir -p $CODE_DIR

    echo "Check out NeverwinterDP and the related projects"
    cd $CODE_DIR
    git clone https://github.com/DemandCube/NeverwinterDP
    cd $CODE_DIR/NeverwinterDP
    ./neverwinterdp.sh checkout

    echo "Build NeverwinterDP and the related projects"
    ./neverwinterdp.sh gradle clean build install -x test

    echo "NeverwinterDP release"
    gradle clean build install release -x test

    RELEASE_PATH=$CODE_DIR/NeverwinterDP/build/release
    cd $HOME_DIR
}

function setup() {

if [ "$RELEASE_PATH" == "" ]; then
    RELEASE_PATH=$WORKING_DIR/build/code/NeverwinterDP/build/release
fi


  rm -rf $WORKING_DIR/DemandCubePlaybooks/roles/neverwinterdp/files
  mkdir -p $WORKING_DIR/DemandCubePlaybooks/roles/neverwinterdp/files
  echo "RELEASE_PATH : $RELEASE_PATH/NeverwinterDP"
  cp -R -p $RELEASE_PATH/NeverwinterDP $WORKING_DIR/DemandCubePlaybooks/roles/neverwinterdp/files

  echo "vagrant flow ansibleinventory" 
  vagrant flow ansibleinventory

  
  if [ "$VM_PROVIDER" == "virtualbox" ]; then
    echo "vagrant flow hostfile"
    vagrant flow hostfile
  else
    echo "vagrant flow hostfile -d"
    vagrant flow hostfile -d
  fi


  echo "vagrant flow playbook"
  vagrant flow playbook
}

function clean() {
    printHeader "Destroy the vm machines and remove all the resources"
    if [ -f "$HOME_DIR/Vagrantfile" ]; then
        echo "Run vagrant destroy"
        vagrant destroy -f
    fi

    rm -rf $HOME_DIR/Vagrantfile
    echo "Remove $HOME_DIR/vagrant-flow_ansible_inventory"
    rm -rf $HOME_DIR/vagrant-flow_ansible_inventory
    echo "Remove $WORKING_DIR"
    rm -rf $WORKING_DIR;

}

function status() {
  printHeader "NeverwinterDP cluster status"
  vagrant status
}

function log() {
  printHeader "NeverwinterDP cluster log"
}

COMMAND=''
VM_PROVIDER="virtualbox"
RELEASE_PATH=
while [ "$1" != "" ]; do
    case $1 in
        -p | --provider )   shift
                            VM_PROVIDER=$1
                            if [ "$VM_PROVIDER" != "virtualbox" ] && [ "$VM_PROVIDER" != "digital_ocean" ]; then
                                echo "Unknown provider"
                                usage
                                exit 1
                            fi
        ;;
        -r | --release )    shift
                            RELEASE_PATH=$1
                            if [ "$RELEASE_PATH" == "" ]; then
                                echo "Unknown release path"
                                usage
                                exit 1
                            fi
        ;;
        initvm )
                            COMMAND='initvm'
        ;;
        setupvm )
                            COMMAND='setup'
        ;;
        status )
                            COMMAND='status'
        ;;
        build )
                            COMMAND='buildNeverwinterDP'
        ;;
        clean )
                            COMMAND='clean'
        ;;
        install )
                            COMMAND='initvm && buildProject && setup'
        ;;
        -h | --help )       usage
                            exit
        ;;
        * )                 usage
                            exit 1
    esac
    shift
done
eval "$COMMAND"

