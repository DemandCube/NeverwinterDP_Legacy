package com.neverwinterdp.cluster.hazelcast;

import org.junit.Test;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class MapUnitTest {
  @Test
  public void run() throws Exception {
    HazelcastInstance instance1 = Hazelcast.newHazelcastInstance(createConfig(5700));
    HazelcastInstance instance2 = Hazelcast.newHazelcastInstance(createConfig(5700));
    System.out.println("Members: "+ instance1.getCluster().getMembers().size()) ;
    
    
    IMap<String, Object> map1 = instance1.getMap("default");
    map1.put("test", "test") ;
    
    IMap<String, Object> map2 = instance2.getMap("default");
    System.out.println("map2 get test = " + map2.get("test"));
    instance1.shutdown(); 
    instance2.shutdown(); 
  }
  
  Config createConfig(int port) {
    Config cfg = new XmlConfigBuilder().build();
    return cfg ; 
  }
}
