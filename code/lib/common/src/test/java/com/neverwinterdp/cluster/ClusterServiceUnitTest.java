package com.neverwinterdp.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerDiscovery;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.ClusterClientHazelcast;
import com.neverwinterdp.server.command.ServerCommand;
import com.neverwinterdp.server.command.ServerCommandResult;
import com.neverwinterdp.server.command.ServerCommands;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;
import com.neverwinterdp.server.command.ServiceCommands;
import com.neverwinterdp.server.config.Configuration;
import com.neverwinterdp.server.service.ServiceDescriptor;
import com.neverwinterdp.server.service.ServiceState;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.JSONSerializer;

public class ClusterServiceUnitTest {
  static protected Server[]      instance ;
  static protected ClusterClient client ;

  @BeforeClass
  static public void setup() throws Exception {
    String jsonConfig = 
      IOUtil.getFileContentAsString("src/test/resources/sample-configuration.json", "UTF-8") ;
    Configuration conf = JSONSerializer.INSTANCE.fromString(jsonConfig, Configuration.class) ;
    instance = new Server[3] ;
    for(int i = 0; i < instance.length; i++) {
      instance[i] = new Server() ;  
      instance[i].setConfig(conf.getServer());
      instance[i].onInit();
      instance[i].getServiceContainer().register(conf.getServices());
      instance[i].start();
    }
    ClusterMember member = instance[1].getClusterRPC().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new ClusterClientHazelcast(connectUrl) ;
  }

  @AfterClass
  static public void teardown() throws Exception {
    client.shutdown(); 
    for(int i = 0; i < instance.length; i++) {
      instance[i].onDestroy();;
    }
    Thread.sleep(1000);
  }
  
  @Test
  public void testPingService() {
    Util.assertServerState(client, ServerState.RUNNING) ;
//    ClusterMember member = instance[0].getClusterRPC().getMember() ;
//    ServerDiscovery sd = client.getServerDiscovery(member);
//    ServiceDescriptor helloService = sd.getServices().get(0) ; 
//    ServiceCommand<ServiceState> ping = new ServiceCommands.Ping().setLogEnable(true) ;
//    ping.setTargetService(helloService);
//    ServiceCommandResult<ServiceState> sel = client.execute(ping, member) ;
//    assertFalse(sel.hasError()) ;
//    assertEquals(ServiceState.START, sel.getResult()) ;
  }
}