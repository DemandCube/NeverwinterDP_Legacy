package com.neverwinterdp.scribengin;

import java.util.List;

import com.neverwinterdp.message.Message;
import com.neverwinterdp.message.MessageLog;
import com.neverwinterdp.util.statistic.StatisticsSet;

public class ReportMessageWriter implements MessageWriter {
  private StatisticsSet statisticsSet = new StatisticsSet();
 
  public void write(Message<?> message) {
    statisticsSet.incr("Message", "all", 1) ;
    List<MessageLog> logs = message.getLogs() ;
    if(logs != null) {
      statisticsSet.incr("MessageLog", "all", 1) ;
      for(int i = 0; i < logs.size(); i++) {
        MessageLog log = logs.get(i) ;
        statisticsSet.incr("MessageLog", log.getServiceId(), 1) ;
      }
    }
  }

  public StatisticsSet getStatistics() { return this.statisticsSet ; }
}