package com.neverwinterdp.queuengin.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner {
  public SimplePartitioner(VerifiableProperties props) {
  }

  public int partition(Object key, int a_numPartitions) {
    String strKey = (String) key ;
    int partition = 0;
    int offset = strKey.lastIndexOf('.');
    if (offset > 0) {
      partition = Integer.parseInt(strKey.substring(offset + 1)) % a_numPartitions;
    }
    return partition;
  }
}