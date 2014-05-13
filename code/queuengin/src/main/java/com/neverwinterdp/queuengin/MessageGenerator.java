package com.neverwinterdp.queuengin;

import com.neverwinterdp.message.Message;

abstract public class MessageGenerator {
  private long numberOfMessages ;
  private long seqTracker ;
  
  public MessageGenerator(long numberOfMessages) {
    this.numberOfMessages = numberOfMessages ;
  }
  
  public boolean hasNext() {
    return seqTracker < numberOfMessages ;
  }
  
  public Message nextMessage() {
    if(seqTracker < numberOfMessages) return createMessage(++seqTracker) ;
    return null ;
  }
  
  public void reset() {
    seqTracker = 0 ;
  }
  
  abstract protected Message createMessage(long seq) ;
  
  public void send(MessageProducer producer, String topic) throws Exception {
    while(hasNext()) {
      producer.send(topic, nextMessage());
    }
  }

  public Thread createSendWorker(final MessageProducer producer, final String topic) {
    Thread thread = new Thread() {
      public void run() {
        try {
          while(hasNext()) {
            producer.send(topic, nextMessage());
          }
        } catch (InterruptedException e) {
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    return thread ; 
  }
}