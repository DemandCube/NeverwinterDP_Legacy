package com.neverwinterdp.ringbearer.job;

import java.io.Serializable;
import java.util.List;

import com.neverwinterdp.ringbearer.job.config.RingBearerJob;

public class RingBearerJobSchedulerInfo implements Serializable {
  private List<RingBearerJob> waittingJobs ;
  private List<RingBearerJob> finishedJobs ;
  private RingBearerJob runningJob ;
  
  public List<RingBearerJob> getWaittingJobs() { return waittingJobs; }
  public void setWaittingJobs(List<RingBearerJob> waittingJobs) { this.waittingJobs = waittingJobs; }
  
  public RingBearerJob getRunningJob() { return runningJob ; }
  public void setRunningJob(RingBearerJob runningJob) { this.runningJob = runningJob; }
  
  public List<RingBearerJob> getFinishedJobs() { return finishedJobs; }
  
  public void setFinishedJobs(List<RingBearerJob> finishedJobs) { this.finishedJobs = finishedJobs; }
}
