package com.neverwinterdp.ringbearer.job;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.neverwinterdp.ringbearer.job.config.RingBearerJob;
import com.neverwinterdp.server.module.ModuleProperties;
import com.neverwinterdp.server.service.AbstractService;
import com.neverwinterdp.util.LoggerFactory;
import com.neverwinterdp.yara.MetricRegistry;

public class RingBearerJobService extends AbstractService {
  private Logger logger ;
  
  @Inject
  private ModuleProperties moduleProperties; 
  
  @Inject
  private MetricRegistry mRegistry ;
  
  private RingBeaderJobScheduler jobScheduler ;
  
  @Inject
  public void init(LoggerFactory factory) {
    logger = factory.getLogger(getClass()) ;
  }

  public boolean submit(RingBearerJob job, long timeout) throws InterruptedException {
    return jobScheduler.submit(job, timeout) ;
  }

  public RingBearerJobSchedulerInfo getSchedulerInfo() {
    return jobScheduler.getInfo() ;
  }
  
  public void start() throws Exception {
    logger.info("Start start()");
    jobScheduler = new RingBeaderJobScheduler(mRegistry) ;
    jobScheduler.start();
    logger.info("Finish start()");
  }

  public void stop() {
    logger.info("Start stop()");
    if(jobScheduler != null) {
      jobScheduler.stop() ;
    }
    logger.info("Finish stop()");
  }
}