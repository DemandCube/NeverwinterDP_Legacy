package com.neverwinterdp.ringbearer.job.send;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neverwinterdp.server.gateway.ClusterGateway;
import com.neverwinterdp.util.monitor.ApplicationMonitor;

public class MessageSender implements Runnable {
  protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
  
  ApplicationMonitor appMonitor ;
  MessageSenderConfig config ;
  ClusterGateway clusterGateway ;
  
  public MessageSender(ApplicationMonitor monitor, MessageSenderConfig config, ClusterGateway clusterGateway ) {
    this.appMonitor = monitor ;
    this.config = config ;
    this.clusterGateway = clusterGateway ;
  }
  
  public void run() {
    ExecutorService taskExecutor = null ;
    try {
      taskExecutor = Executors.newFixedThreadPool(config.numOfProcesses);
      MessageSenderTask[] task = config.createMessageSender(appMonitor) ;
      for(int i = 0; i < task.length; i++) {
        task[i].setLogger(LOGGER) ;
        taskExecutor.submit(task[i]) ;
      }
      taskExecutor.shutdown();
      taskExecutor.awaitTermination(config.maxDuration, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
    } catch(Exception ex) {
      LOGGER.error("Unexpected error", ex);
    } finally {
      if(taskExecutor != null) {
        taskExecutor.shutdownNow() ;
      }
    }
  }
}
