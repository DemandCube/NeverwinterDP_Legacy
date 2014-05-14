package com.neverwinterdp.cluster.hazelcast;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;

public class TaskUnitTest  {
  HazelcastInstance instance1, instance2, instance3 ;
  
  @Before
  public void setup() {
    // Configure
    Config config = new XmlConfigBuilder().build();
    // Set call timeout to 5 seconds to make the problem appear quicker
    config.setProperty("hazelcast.operation.call.timeout.millis", "5000");

    // Create two Hazelcast instances
    instance1 = Hazelcast.newHazelcastInstance(config);
    instance2 = Hazelcast.newHazelcastInstance(config);
    instance3 = Hazelcast.newHazelcastInstance(config);
  }
  
  @After
  public void teardown() {
    instance1.getLifecycleService().shutdown();
    instance2.getLifecycleService().shutdown();
    instance3.getLifecycleService().shutdown();
  }
  
  @Test
  public void testGetOnLongRunningTask() throws Exception {
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