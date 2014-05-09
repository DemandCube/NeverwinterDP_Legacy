package com.neverwinterdp.queuengin;

import com.neverwinterdp.testframework.event.SampleEvent;

public class ReportMessageConsumerHandler implements MessageConsumerHandler<SampleEvent> {
  private int count =  0 ;
  private int errorCount = 0;
  
  public int messageCount() { return count ; }
  
  public int errorMessageCount() { return errorCount ; }
  
  public void onMessage(Message<SampleEvent> jsonMessage) {
    count++ ;
    try {
      SampleEvent event = jsonMessage.getDataAs(SampleEvent.class);
      System.out.println("Consume: " + event.getDescription());
      if(jsonMessage.getLogs() != null) {
        for(MessageLog log : jsonMessage.getLogs()) {
          System.out.println("  " + log.toString()) ;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onErrorMessage(Message<SampleEvent> message, Throwable error) {
  }
}