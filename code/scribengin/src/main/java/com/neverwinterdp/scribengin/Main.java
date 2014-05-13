package com.neverwinterdp.scribengin;

import com.neverwinterdp.queuengin.kafka.KafkaMessageConsumerConnector;

public class Main {
  static String getValue(String arg) {
    String[] array = arg.split("=", 2) ;
    return array[1] ;
  }
  
  static public void main(String[] args) throws Exception {
    String topic = "HelloSparkngin" ;
    String zookeeperList = "127.0.0.1:2181" ;
    long reportPeriod = 5000 ;
    System.out.println("Available options: ");
    System.out.println("  --topic=topic");
    System.out.println("  --zookeeper-list=127.0.0.1:2181");
    System.out.println("  --report-period=5000");

    if(args != null) {
      for(String arg : args) {
        if(arg.startsWith("--topic")) topic = getValue(arg) ;
        else if(arg.startsWith("--zookeeper-list")) zookeeperList = getValue(arg) ;
        else if(arg.startsWith("--report-period")) reportPeriod = Long.parseLong(getValue(arg)) ;
        else {
          System.out.println("Unknown option: " + arg);
          return ;
        }
      }
    }
    
    ReportMessageWriter writer = new ReportMessageWriter() ;
    ScribeMessageHandler handler = new  ScribeMessageHandler(writer) ; 
    
    KafkaMessageConsumerConnector connector = new KafkaMessageConsumerConnector("Scribengin", zookeeperList) ;
    connector.consume(topic, handler, 1) ;
    
    while(true) {
      System.out.println("Report") ;
      System.out.println("---------------------------------------------------") ;
      writer.getStatistics().report(System.out, "asc");
      Thread.sleep(reportPeriod) ;
    }
  }
}