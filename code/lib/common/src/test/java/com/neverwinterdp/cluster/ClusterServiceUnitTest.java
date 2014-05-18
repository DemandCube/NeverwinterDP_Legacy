package com.neverwinterdp.cluster;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.ServerRegistration;
import com.neverwinterdp.server.ServerState;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterEvent;
import com.neverwinterdp.server.cluster.ClusterListener;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.cluster.hazelcast.HazelcastClusterClient;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommandResult;
import com.neverwinterdp.server.command.ServiceCommands;
import com.neverwinterdp.server.config.Configuration;
import com.neverwinterdp.server.service.ServiceRegistration;
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
    ClusterMember member = instance[1].getCluster().getMember() ;
    String connectUrl = member.getIpAddress() + ":" + member.getPort() ;
    client = new HazelcastClusterClient(connectUrl) ;
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
    ClusterMember member = instance[0].getCluster().getMember() ;
    ServerRegistration serverRegistration = client.getServerRegistration(member);
    ServiceRegistration helloService = serverRegistration.getServices().get(0) ; 
    
    ServiceCommand<ServiceState> ping = new ServiceCommands.Ping().setLogEnable(true) ;
    ping.setTargetService(helloService);
    ServiceCommandResult<ServiceState> sel = client.execute(ping, member) ;
    assertFalse(sel.hasError()) ;
    assertEquals(ServiceState.START, sel.getResult()) ;
  }
  
  @Test
  public void testStopStartService() throws Exception {
    ServiceClusterListener<ClusterClient> listener = new ServiceClusterListener<ClusterClient>() ;
    client.addListener(listener);
    
    ClusterMember member = instance[0].getCluster().getMember() ;
    ServerRegistration serverRegistration = 
      client.getClusterRegistration().getServerRegistration(member);
    ServiceRegistration helloService = serverRegistration.getServices().get(0) ; 
    
    ServiceCommand<ServiceRegistration> stop = new ServiceCommands.Stop().setLogEnable(true) ;
    stop.setTargetService(helloService);
    ServiceCommandResult<ServiceRegistration> stopResult = client.execute(stop, member) ;
    assertEquals(ServiceState.STOP, stopResult.getResult().getState()) ;
    //wait to make sure the client get a cluster service stop event notification
    Thread.sleep(100l);
    assertEquals(1, listener.events.size()) ;
    
    ServiceCommand<ServiceRegistration> start = new ServiceCommands.Start().setLogEnable(true) ;
    start.setTargetService(helloService);
    ServiceCommandResult<ServiceRegistration> startResult = client.execute(start, member) ;
    assertEquals(ServiceState.START, startResult.getResult().getState()) ;
    //wait to make sure the client get a cluster service stop event notification
    Thread.sleep(100l);
    assertEquals(2, listener.events.size()) ;
    
    client.removeListener(listener);
  }
  
  static public class ServiceClusterListener<T> implements ClusterListener<T> {
    List<ClusterEvent> events = new ArrayList<ClusterEvent>() ;
    
    public void onEvent(T listener, ClusterEvent event) { 
      if(event.getType().equals(ClusterEvent.ServiceStateChange)) {
        events.add(event) ;
      }
    }
  }
}