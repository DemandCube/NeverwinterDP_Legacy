package com.neverwinterdp.server.shell;

import com.beust.jcommander.Parameter;
import com.neverwinterdp.ringbearer.job.send.MessageSender;
import com.neverwinterdp.ringbearer.job.send.MessageSenderConfig;
import com.neverwinterdp.ringbearer.job.simulation.ServiceFailureSimulator;
import com.neverwinterdp.server.gateway.Command;
import com.neverwinterdp.yara.MetricPrinter;
import com.neverwinterdp.yara.MetricRegistry;

@ShellCommandConfig(name = "ringbearer:job")
public class RingBearerJobCommand extends ShellCommand {
  public RingBearerJobCommand() {
    add("send", SendCommand.class) ;
    add("simulation", SimulationCommand.class) ;
  }
  
  static public class SendCommand extends ShellSubCommand {
    public void execute(Shell shell, ShellContext ctx, Command command) throws Exception {
      MessageSenderConfig config = new MessageSenderConfig() ;
      command.mapAll(config);
      MetricRegistry mRegistry = new MetricRegistry() ;
      MessageSender sender = new MessageSender(mRegistry, config, ctx.getClusterGateway()) ;
      sender.run();
      new MetricPrinter(ctx.console().getConsoleAppendable()).print(mRegistry); ;
    }
  }
  
  static public class SimulationCommand extends ShellSubCommand {
    @Parameter(
        names = "--name", description = "Simulation name"
    )
    private String name ;
    
    public void execute(Shell shell, ShellContext ctx, Command command) throws Exception {
      command.mapPartial(this);
      if("service-failure".equals(ServiceFailureSimulator.NAME)) {
        ServiceFailureSimulator simulator = new ServiceFailureSimulator(ctx) ;
        command.mapAll(simulator);
        simulator.schedule(ctx.getTimer());
      }
    }
  }
}