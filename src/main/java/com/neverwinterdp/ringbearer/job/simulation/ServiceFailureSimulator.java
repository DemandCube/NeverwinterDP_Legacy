package com.neverwinterdp.ringbearer.job.simulation;

import java.util.Timer;
import java.util.TimerTask;

import com.beust.jcommander.Parameter;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.cluster.ClusterMember;
import com.neverwinterdp.server.command.ServiceCommandResult;
import com.neverwinterdp.server.command.ServiceCommands;
import com.neverwinterdp.server.gateway.MemberSelector;
import com.neverwinterdp.server.service.ServiceRegistration;
import com.neverwinterdp.server.shell.Console;
import com.neverwinterdp.server.shell.ShellContext;

public class ServiceFailureSimulator extends TimerTask {
  final static public String NAME = "service-failure" ;
  
  @Parameter(names = "--module", required=true, description = "Target module")
  private String  module ;
  
  @Parameter(names = "--service-id", required=true, description = "Target service id")
  private String  serviceId ;
  
  @Parameter(names = "--delay", description = "Delay simulation time")
  private long    delay = 1000;
  
  @Parameter(names = "--period", description = "repeat simulation period")
  private long    period = 30000;
  
  @Parameter(names = "--failure-time", description = "repeat simulation period")
  private long    faillureTime = 1000;
  
  @Parameter(names = "--target-member-role", description = "repeat simulation period")
  private String    targetMemberRole ;
    
  @Parameter(names = "--target-member-name", description = "repeat simulation period")
  private String    targetMemberName ;
  
  private ShellContext shellContext ;
  
  public ServiceFailureSimulator(ShellContext shellContext ) {
    this.shellContext = shellContext ;
  }
  
  public long getRepeatPeriod() { return period ; }
  
  public void schedule(Timer timer) {
    timer.schedule(this, delay, period) ;
  }
  
  public void run() {
    if(shellContext == null || shellContext.isClose()) {
      System.out.println("Cluster is not available!!!");
      return ;
    }
    Console out = shellContext.console() ;
    try {
      ClusterClient client = shellContext.getClusterGateway().getClusterClient() ;
      MemberSelector memberSelector = new MemberSelector();
      memberSelector.memberName = targetMemberName; 
      memberSelector.memberRole = targetMemberRole; 
      ClusterMember member = memberSelector.selectRandomMember(client) ;
      ServiceCommands.Stop stop = new ServiceCommands.Stop() ;
      stop.setTargetService(module, serviceId);
      ServiceCommandResult<ServiceRegistration> stopResult = client.execute(stop, member) ;
      
      out.println("ServiceFailureSimulator: Stop " + serviceId + ", member = " + member.getMemberName() + ", service state = " + stopResult.getResult().getState());
      if(stopResult.hasError()) this.cancel() ;
      
      Thread.sleep(faillureTime);
      
      ServiceCommands.Start start = new ServiceCommands.Start() ;
      start.setTargetService(module, serviceId);
      ServiceCommandResult<ServiceRegistration> startResult = client.execute(start, member) ;
      out.println("ServiceFailureSimulator: Start " + serviceId + ", member = " + member.getMemberName() + ", service state = " + startResult.getResult().getState());
      if(startResult.hasError()) this.cancel() ;
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}