package com.neverwinterdp.server.shell;

import com.neverwinterdp.ringbearer.job.RingBearerJobSchedulerInfo;
import com.neverwinterdp.server.command.ServiceCommandResult;
import com.neverwinterdp.server.gateway.Command;
import com.neverwinterdp.util.JSONSerializer;
import com.neverwinterdp.util.text.TabularPrinter;

@ShellCommandConfig(name = "ringbearer")
public class RingBearerCommand extends ShellCommand {
  public RingBearerCommand() {
    add("submit", SubmitCommand.class);
    add("scheduler", SchedulerCommand.class);
  }
  
  static public class SubmitCommand extends ShellSubCommand {
    public void execute(Shell shell, ShellContext ctx, Command command) throws Exception {
      ServiceCommandResult<Boolean>[] results = ctx.getClusterGateway().execute(command) ;
      
      ctx.console().header("Submit job");
      TabularPrinter printer = ctx.console().tabularPrinter(30, 10, 10) ;
      printer.setIndent("  ") ;
      printer.header("Member", "Error", "Submit");
      boolean hasError = false ;
      for(int i = 0; i < results.length; i++) {
        ServiceCommandResult<Boolean> sel = results[i] ;
        printer.row(sel.getFromMember(), sel.hasError(), sel.getResult());
        if(sel.hasError()) hasError = true ;
      }
      if(hasError) {
        for(int i = 0; i < results.length; i++) {
          ServiceCommandResult<Boolean> sel = results[i] ;
          if(sel.hasError()) ctx.console().println(sel.getError());
        }
      }
    }
  }
  
  static public class SchedulerCommand extends ShellSubCommand {
    public void execute(Shell shell, ShellContext ctx, Command command) throws Exception {
      ServiceCommandResult<RingBearerJobSchedulerInfo>[] results = ctx.getClusterGateway().execute(command) ;
      ctx.console().header("Get RingBearer Scheduler Job");
      for(int i = 0; i < results.length; i++) {
        ServiceCommandResult<RingBearerJobSchedulerInfo> sel = results[i] ;
        String json = JSONSerializer.INSTANCE.toString(sel);
        ctx.console().println("\n" + json + "\n");
      }
    }
  }
}