package com.neverwinterdp.ringbearer.job.send;

import java.io.Serializable;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.neverwinterdp.yara.MetricRegistry;

public class MessageSenderConfig implements Serializable {
  @Parameter(
      names = "--num-of-task", description = "The number of tasks"
  )
  public int numOfTasks = 1 ;
    
  @Parameter(
      names = "--num-of-process", description = "The number of processes"
  )
  public int numOfProcesses = 1 ;
  
  
  @Parameter(
    names = "--message-size", description = "The size of the message in bytes"
  )
  public int    messageSize = 1024 ;
  
  @Parameter(
    names = "--max-duration", description = "Maximum duration time of each task"
  )
  public long   maxDuration = 30 * 1000 ; // 30s
  
  @Parameter(
    names = "--max-num-of-message", description = "Maximum number of message for all tasks"
  )
  public long   maxNumOfMessage = 1000000; //1 million messages by default
  
  @Parameter(
    names = "--send-period", description = "Send period, use with the periodic task"
  )
  public long   sendPeriod = 0 ; // Every 100 ms
  
  @ParametersDelegate
  final public MessageDriverConfig driverConfig = new MessageDriverConfig() ;
  
  public MessageSenderTask createMessageSender(MetricRegistry appMonitor, String taskId) {
    MessageSenderTask sender = new MessageSenderTask(taskId, appMonitor, this) ;
    return sender ;
  }
  
  public MessageSenderTask[] createMessageSender(MetricRegistry appMonitor) {
    MessageSenderTask[] tasks = new MessageSenderTask[numOfTasks];
    for(int i = 0; i < numOfTasks; i++) {
      tasks[i] = new MessageSenderTask("message-sender-task-" + i, appMonitor, this) ;
    }
    return tasks ;
  }
}