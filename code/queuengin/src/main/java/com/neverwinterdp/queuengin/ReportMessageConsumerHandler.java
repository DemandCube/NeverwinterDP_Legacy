package com.neverwinterdp.queuengin;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.message.MessageTrace;
import com.neverwinterdp.testframework.event.SampleEvent;

public class ReportMessageConsumerHandler implements MessageConsumerHandler {
  private int count =  0 ;
  private int errorCount = 0;
  
  public int messageCount() { return count ; }
  
  public int errorMessageCount() { return errorCount ; }
  
  public void onMessage(Message msg) {
    count++ ;
    try {
      SampleEvent event = msg.getData().getDataAs(SampleEvent.class);
      System.out.println("Consume: " + event.getDescription());
      if(msg.getTraces() != null) {
        for(MessageTrace log : msg.getTraces()) {
          System.out.println("  " + log.toString()) ;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onErrorMessage(Message message, Throwable error) {
  }
}