package com.neverwinterdp.ringbearer.job;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neverwinterdp.ringbearer.RingBearerClusterBuilder;
import com.neverwinterdp.server.shell.Shell;
import com.neverwinterdp.sparkngin.KafkaMessageForwarder;
import com.neverwinterdp.util.IOUtil;
/**
 * @author Tuan Nguyen
 * @email  tuan08@gmail.com
 */
public class RingBearerJobUnitTest {
  static RingBearerClusterBuilder clusterBuilder ;
  static String TOPIC = RingBearerClusterBuilder.TOPIC ;
  
  @BeforeClass
  static public void setup() throws Exception {
    clusterBuilder = new RingBearerClusterBuilder(KafkaMessageForwarder.class.getName(), 2) ;
    clusterBuilder.init() ;
  }

  @AfterClass
  static public void teardown() throws Exception {
    clusterBuilder.destroy() ; 
  }
 
  @Test
  public void testRingBearerJobCommands() throws Exception {
    clusterBuilder.install() ; 
    Thread.sleep(10000);
    Shell shell = new Shell() ;
    shell.getShellContext().connect();
    //shell.execute("service restart --member-role kafka --cleanup --module Kafka --service-id KafkaClusterService");
    shell.execute("ringbearer:job send --max-num-of-message 1000") ;
    clusterBuilder.shell.exec("server metric");
//    shell.execute(
//        "ringbearer:job simulation " +
//        "  --name service-failure --target-member-role kafka " + 
//        "  --module Kafka --service-id KafkaClusterService --delay 0 --period 3000 --failure-time 1000");
    shell.execute(
      "ringbearer:job send " + 
      "  --driver kafka " + 
      "  --broker-connect " + clusterBuilder.getKafkaConnect() + 
      "  --topic metrics.consumer --max-num-of-message 50000"
    );
    shell.close() ;
    //wait to make sure that the client threads are disconnected and closed
    Thread.sleep(3000);
    clusterBuilder.shell.exec("server metric");
    clusterBuilder.uninstall();
  }
  
  @Test
  public void testRingBearerJobService() throws Exception {
    String script = IOUtil.getFileContentAsString("src/test/resources/ringbearerjob.js") ;
    clusterBuilder.install() ; 
    Thread.sleep(3000);
    Shell shell = new Shell() ;
    shell.getShellContext().connect();
    shell.execute("ringbearer submit --member-name ringbearer --description \"This is a hello job\" #{data " + script + " }#");
    Thread.sleep(10000);
    shell.execute("ringbearer scheduler --member-name ringbearer");
    shell.close() ;
    clusterBuilder.uninstall();
  }
}