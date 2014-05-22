package com.neverwinterdp.sparkngin.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.neverwinterdp.message.Message;

public class MessageForwarderQueue {
  private MessageForwarder forwarder ;
  private BlockingQueue<Message> queue ;
  private Thread forwarderThread ;
  private int wakeupCount = 0;
  
  public MessageForwarderQueue(MessageForwarder mforwarder, int bufferSize) {
    this.forwarder = mforwarder ;
    queue = new LinkedBlockingQueue<Message>(bufferSize) ;
    forwarderThread = new Thread("") {
      public void run() {
        try {
          while(true) {
            Message message = queue.peek() ;
            if(message == null) {
              synchronized(queue) {
                queue.wait();
                wakeupCount++ ;
                //System.out.println("wake up when a new message is available, count = " + wakeupCount);
              }
            } else {
              try {
              forwarder.forward(message);
              } catch(Exception ex) {
                ex.printStackTrace();
                return ;
              }
              //if forward successfully, remove the message from queue.
              queue.remove();
            }
          }
        } catch(InterruptedException interrupt) {
        }
      }
    };
    forwarderThread.start() ;
  }
  
  public void put(Message message) throws InterruptedException {
    synchronized(queue) {
      queue.put(message) ;
      queue.notify();
    }
  }
}
