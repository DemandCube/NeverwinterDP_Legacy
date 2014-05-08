package com.neverwinterdp.queuengin;

import com.neverwinterdp.queuengin.JSONMessage.Log;
import com.neverwinterdp.testframework.event.SampleEvent;

public class DummyJSONMessageConsumerHandler implements JSONMessageConsumerHandler<SampleEvent> {
  private int count =  0 ;
 
  public int messageCount() { return count ; }
  
  public void onJSONMessage(JSONMessage<SampleEvent> jsonMessage) {
    count++ ;
    try {
      SampleEvent event = jsonMessage.getDataAs(SampleEvent.class);
      System.out.println("Consume: " + event.getDescription());
      if(jsonMessage.getLogs() != null) {
        for(Log log : jsonMessage.getLogs()) {
          System.out.println("  " + log.toString()) ;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}