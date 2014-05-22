package com.neverwinterdp.server;

public class ServerRuntimeEnvironment {
  static int serverIdTracker ;
  
  private String appDir;
  private String configDir;
  private String logDir;
  private String tmpDir;
  private String workingDir;
  private String dataDir;
  private int    serverId ;

  public ServerRuntimeEnvironment(String appDir) {
    appDir = getSystemProperty("app.dir", "./") ;
    configDir = getSystemProperty("app.config.dir", appDir + "/config") ;
    logDir = getSystemProperty("app.log.dir", appDir + "/logs") ;
    tmpDir = getSystemProperty("app.tmp.dir", appDir + "/tmp") ;
    workingDir = getSystemProperty("app.working.dir", appDir + "/working") ;
    dataDir = getSystemProperty("app.data.dir", appDir + "/data") ;
    synchronized(ServerRuntimeEnvironment.class) {
      serverId = ++serverIdTracker ;
    }
  }
  
  public int getServerId() { return this.serverId ; }
  
  public String getAppDir() {
    return appDir;
  }

  public void setAppDir(String appDir) {
    this.appDir = appDir;
  }

  public String getConfigDir() {
    return configDir;
  }

  public void setConfigDir(String configDir) {
    this.configDir = configDir;
  }

  public String getLogDir() {
    return logDir;
  }

  public void setLogDir(String logDir) {
    this.logDir = logDir;
  }

  public String getTmpDir() {
    return tmpDir;
  }

  public void setTmpDir(String tmpDir) {
    this.tmpDir = tmpDir;
  }
  
  public String getWorkingDir() {
    return workingDir;
  }

  public void setWorkingDir(String workingDir) {
    this.workingDir = workingDir;
  }

  public String getDataDir() {
    return dataDir;
  }

  public void setDataDir(String dataDir) {
    this.dataDir = dataDir;
  }
  
  String getSystemProperty(String name, String defaultValue) {
    String value = System.getProperty(name) ;
    if(value != null) return value ;
    return defaultValue ;
  }
}
