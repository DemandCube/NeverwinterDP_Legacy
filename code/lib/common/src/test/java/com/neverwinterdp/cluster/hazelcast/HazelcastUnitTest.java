package com.neverwinterdp.cluster.hazelcast;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

public class HazelcastUnitTest  {
  static HazelcastInstance instance1, instance2, instance3 ;
  
  @BeforeClass
  static public void setup() {
    // Configure
    Config config = new XmlConfigBuilder().build();
    // Set call timeout to 5 seconds to make the problem appear quicker
    config.setProperty("hazelcast.operation.call.timeout.millis", "5000");

    // Create two Hazelcast instances
    instance1 = Hazelcast.newHazelcastInstance(config);
    instance2 = Hazelcast.newHazelcastInstance(config);
    instance3 = Hazelcast.newHazelcastInstance(config);
    System.out.println("\n\nsetup\n\n");
  }
  
  @AfterClass
  static public void teardown() {
    System.out.println("teardown");
    instance1.getLifecycleService().shutdown();
    instance2.getLifecycleService().shutdown();
    instance3.getLifecycleService().shutdown();
  }
  
  @Test
  public void testMap() throws Exception {
    System.out.println("Members: "+ instance1.getCluster().getMembers().size()) ;
    
    
    IMap<String, Object> map1 = instance1.getMap("default");
    IMap<String, Object> map2 = instance2.getMap("default");
    
    map1.put("test", "test") ;
    assertEquals("test", map2.get("test")) ;
  }
  
  @Test
  public void testTask() throws Exception {
    // Submit the hello task on instance 1
    IExecutorService exService = instance1.getExecutorService("default");
    instance2.getCluster().getLocalMember().setStringAttribute("instance", "instance2");
    Map<Member, Future<Object>> futures = exService.submitToAllMembers(new HelloTask());
    Iterator<Future<Object>> itr = futures.values().iterator() ;
    while(itr.hasNext()) {
      Object result = itr.next().get(25, TimeUnit.SECONDS);
      System.out.println(result);
    }
  }

  private static class HelloTask implements Callable<Object>, Serializable, HazelcastInstanceAware {
    private static final long serialVersionUID = 1L;

    transient private HazelcastInstance hazelcastInstance ;
    
    public Object call() throws Exception {
      Member member = hazelcastInstance.getCluster().getLocalMember() ;
      System.out.println(member + " task: Started. Sleep for 1 seconds");
      System.out.println(member + " instance attribute = " + member.getStringAttribute("instance"));
      Thread.sleep(1000);
      System.out.println(member + "task: Finished");
      return "Hello from " + member;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
      this.hazelcastInstance = hazelcastInstance ;
    }
  }
}