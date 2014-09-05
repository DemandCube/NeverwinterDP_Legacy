package com.neverwinterdp.ringbearer.job;

import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.ringbearer.RingBearerClusterBuilder;
import com.neverwinterdp.server.shell.Shell;
import com.neverwinterdp.sparkngin.KafkaMessageForwarder;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class RingBearerKafkaJobLoadTest {
  static RingBearerClusterBuilder clusterBuilder ;
  static String TOPIC = RingBearerClusterBuilder.TOPIC ;
  
  @BeforeClass
  static public void setup() throws Exception {
    clusterBuilder = new RingBearerClusterBuilder(KafkaMessageForwarder.class.getName(), 3) ;
    clusterBuilder.init() ;
  }

  @AfterClass
  static public void teardown() throws Exception {
    clusterBuilder.destroy() ; 
  }
 
  @Test
  public void testRingBearerJobCommands() throws Exception {
    clusterBuilder.install() ; 
    Shell shell = new Shell() ;
    shell.getShellContext().connect();
    shell.execute(
      "ringbearer:job simulation " +
      "  --name service-failure --target-member-role kafka " + 
      "  --module Kafka --service-id KafkaClusterService --delay 0 --period 5000 --failure-time 5000");
    
    shell.execute(
    "ringbearer:job send " + 
    "  --driver kafka " + 
    "  --broker-connect " + clusterBuilder.getKafkaConnect() + 
    "  --topic metrics.consumer --max-num-of-message 300000 --max-duration 1500000"
    );

    clusterBuilder.shell.exec("server metric");
    shell.close() ;
    Thread.sleep(15000);
    clusterBuilder.shell.exec("server metric");
    clusterBuilder.uninstall();
    
    Thread.sleep(2000);
    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
    for(Thread thread : threadSet) {
      if(!thread.isAlive()) continue ;
      String gname = thread.getThreadGroup().getName() ;
      if(!gname.startsWith("Kafka")) continue ;
      System.out.println("THREAD: " + thread.getName() + ", group = " + thread.getThreadGroup().getName() + ", deamon = " + thread.isDaemon() + ", alive " + thread.isAlive());
    }
  }
}