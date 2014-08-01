package com.neverwinterdp.server.module;

import java.util.Map;

import com.neverwinterdp.ringbearer.job.RingBearerJobService;

@ModuleConfig(name = "RingBearer", autostart = false, autoInstall=false)
public class RingBearerModule extends ServiceModule {
  
  protected void configure(Map<String, String> properties) {  
    bindService(RingBearerJobService.class) ;
  }
}