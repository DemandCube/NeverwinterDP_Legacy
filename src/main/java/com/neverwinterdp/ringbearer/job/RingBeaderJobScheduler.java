package com.neverwinterdp.ringbearer.job;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.neverwinterdp.ringbearer.job.config.RingBearerJob;
import com.neverwinterdp.server.shell.Shell;
import com.neverwinterdp.util.monitor.ApplicationMonitor;

public class RingBeaderJobScheduler {
  private AtomicLong idTracker = new AtomicLong() ;
  private ApplicationMonitor appMonitor ;
  private BlockingQueue<RingBearerJob> jobQueue = new LinkedBlockingQueue<RingBearerJob>() ;
  private Map<String, RingBearerJob> finishedJobs = new LinkedHashMap<String, RingBearerJob>() ;
  private RingBearerJob  runningJob = null ;
  private JobSchedulerThread schedulerThread; 

  public RingBeaderJobScheduler(ApplicationMonitor appMonitor) {
    this.appMonitor = appMonitor ;
  }
  
  public boolean submit(RingBearerJob job, long timeout) throws InterruptedException {
    job.setId(Long.toString(idTracker.incrementAndGet()));
    return jobQueue.offer(job, timeout, TimeUnit.MILLISECONDS) ;
  }
  
  public List<RingBearerJob> getWaittingJobs() {
    List<RingBearerJob> holder = new ArrayList<RingBearerJob>() ;
    Iterator<RingBearerJob> i = jobQueue.iterator() ;
    while(i.hasNext()) holder.add(i.next()) ;
    return holder ;
  }
  
  public List<RingBearerJob> getfinishedJobs() {
    List<RingBearerJob> holder = new ArrayList<RingBearerJob>() ;
    Iterator<RingBearerJob> i = finishedJobs.values().iterator() ;
    while(i.hasNext()) holder.add(i.next()) ;
    return holder ;
  }
  
  public RingBearerJob getRunningJob() { return this.runningJob  ; }
  
  public RingBearerJobSchedulerInfo getInfo() {
    RingBearerJobSchedulerInfo info = new RingBearerJobSchedulerInfo() ;
    info.setRunningJob(getRunningJob());
    info.setWaittingJobs(getWaittingJobs());
    info.setFinishedJobs(getfinishedJobs());
    return info ;
  }
  
  public void start() {
    this.schedulerThread = new JobSchedulerThread() ;
    this.schedulerThread.start() ;
  }
  
  public void stop() {
    if(schedulerThread != null && schedulerThread.isAlive()) {
      schedulerThread.interrupt() ;
    }
  }
  
  public class JobSchedulerThread extends Thread {
    public void run() {
      RingBearerJobRunner jobRunner = null ;
      RingBearerJob job = null ;
      try {
        while((job = jobQueue.take()) != null) {
          runningJob = job ;
          jobRunner = new RingBearerJobRunner(job) ;
          jobRunner.start(); 
          while(jobRunner.isAlive()) {
            Thread.sleep(100);
          }
          finishedJobs.put(runningJob.getId(), runningJob) ;
          runningJob = null;
        }
      } catch (InterruptedException e) {
        if(jobRunner != null) jobRunner.interrupt();
      }
    }
  }
  
  public class RingBearerJobRunner extends Thread {
    RingBearerJob job ;
    
    public RingBearerJobRunner(RingBearerJob job) {
      this.job = job ;
    }
    
    public void run() {
      Shell shell = new Shell() ;
      try {
        shell.getShellContext().connect();
        ByteArrayOutputStream bout = new ByteArrayOutputStream() ;
        PrintStream pout = new PrintStream(bout) ;
        shell.getShellContext().console().setPrintStream(pout, System.out);
        shell.evalJScript(job.getScript(), job.getScriptProperties()) ;
        job.setOutputAttribute("consoleOutput", new String(bout.toByteArray()));
      } catch(Throwable t) {
        job.setOutputAttribute("error", t);
        t.printStackTrace(); 
      } finally {
        shell.close() ;
      }
    }
  }
}