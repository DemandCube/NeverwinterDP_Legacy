package com.neverwinterdp.server.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.neverwinterdp.ringbearer.job.RingBearerJobSchedulerInfo;
import com.neverwinterdp.ringbearer.job.RingBearerJobService;
import com.neverwinterdp.ringbearer.job.config.RingBearerJob;
import com.neverwinterdp.server.cluster.ClusterClient;
import com.neverwinterdp.server.command.ServiceCommand;
import com.neverwinterdp.server.command.ServiceCommands;
import com.neverwinterdp.util.IOUtil;
import com.neverwinterdp.util.text.StringUtil;

@CommandPluginConfig(name = "ringbearer")
public class RingBearerCommandPlugin extends CommandPlugin {
  public RingBearerCommandPlugin() {
    add("submit", new submit()) ;
    add("scheduler", new scheduler()) ;
  }
  
  static public class submit implements SubCommandExecutor {
    @Parameter(names = {"--description"} , description = "Job description")
    private String description ;
    
    @DynamicParameter(names = "-P", description = "The dynamic properties for the script")
    private Map<String, Object> scriptProperties = new HashMap<String, Object>();
    
    @Parameter(names = {"-f", "--file"} , description = "The RingBearerJob in a js file")
    private String file ;
    
    @Parameter(description = "The RingBearerJob in a json format")
    private List<String> jsonData  = new ArrayList<String>() ;
    
    public Object execute(ClusterClient clusterClient, Command command) throws Exception {
      command.mapAll(this);
      String script = null ;
      if(file != null) {
        script = IOUtil.getFileContentAsString(file) ;
      } else {
        script = StringUtil.join(jsonData, " ") ;
      }
      System.out.println("JS INPUT " + script) ;
      RingBearerJob job = new RingBearerJob() ;
      job.setDescription(description);
      job.setScript(script);
      job.setScriptProperties(scriptProperties);
      ServiceCommand<Boolean> methodCall = 
          new ServiceCommands.MethodCall<Boolean>("submit", job, command.getMemberSelector().timeout) ;
      methodCall.setTargetService("RingBearer", RingBearerJobService.class.getSimpleName());
      return  command.getMemberSelector().execute(clusterClient, methodCall) ;
    }
  }
 
  static public class scheduler implements SubCommandExecutor {
    public Object execute(ClusterClient clusterClient, Command command) throws Exception {
      ServiceCommand<RingBearerJobSchedulerInfo> methodCall = 
          new ServiceCommands.MethodCall<RingBearerJobSchedulerInfo>("getSchedulerInfo") ;
      methodCall.setTargetService("RingBearer", RingBearerJobService.class.getSimpleName());
      return command.getMemberSelector().execute(clusterClient, methodCall) ;
    }
  }
}