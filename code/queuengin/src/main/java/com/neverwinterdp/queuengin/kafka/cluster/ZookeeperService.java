package com.neverwinterdp.queuengin.kafka.cluster;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.server.DatadirCleanupManager;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.config.ServiceConfig;
import com.neverwinterdp.server.service.AbstractService;

public class ZookeeperService extends AbstractService {
  private String configDir ;
  private ZookeeperLaucher launcher ;
  private Thread zkThread ;
  
  public void onInit(Server server) {
    super.onInit(server); 
    configDir = server.getRuntimeEnvironment().getConfigDir() ;
  }
  
  public void start() {
    if (launcher != null) {
      throw new IllegalStateException("ZookeeperLaucher should be null");
    }
    
    zkThread = new Thread() {
      public void run() {
        try {
          ServiceConfig config = getServiceConfig();
          String zookeeperConfigPath = (String)config.getParameters().get("zookeeperConfigPath") ;
          Properties zkProperties = new Properties();
          zkProperties.load(new FileInputStream(configDir + "/" + zookeeperConfigPath));
          launcher = new QuorumPeerMainExt().start(zkProperties) ;
        } catch (Exception ex) {
          launcher = null;
          logger.error("Cannot lauch the ZookeeperService", ex);
          throw new RuntimeException("Cannot lauch the ZookeeperService", ex);
        }
      }
    };
    zkThread.start() ;
  }

  public void stop() {
    if (launcher != null) {
      launcher.shutdown();
      launcher = null;
      if(zkThread.isAlive()) zkThread.interrupt() ;;
    }
  }

  static public interface ZookeeperLaucher {
    public void shutdown() ;
  }
  
  public class QuorumPeerMainExt extends QuorumPeerMain implements ZookeeperLaucher {
    public ZookeeperLaucher start(Properties zkProperties) throws ConfigException, IOException {
      QuorumPeerConfig zkConfig = new QuorumPeerConfig();
      zkConfig.parseProperties(zkProperties);
      DatadirCleanupManager purgeMgr = new DatadirCleanupManager(
          zkConfig.getDataDir(), 
          zkConfig.getDataLogDir(), 
          zkConfig.getSnapRetainCount(), 
          zkConfig.getPurgeInterval());
      purgeMgr.start();

      if (zkConfig.getServers().size() > 0) {
        runFromConfig(zkConfig);
        return this ;
      } else {
        logger.warn(
          "Either no config or no quorum defined in config, running " + 
          " in standalone mode"
        );
        // there is only server in the quorum -- run as standalone
        ZooKeeperServerMainExt laucher = new ZooKeeperServerMainExt() ;
        return laucher.start(zkConfig) ;
      }
    }
    
    public void shutdown() {
      quorumPeer.shutdown();
    }
  }
  
  public class ZooKeeperServerMainExt extends ZooKeeperServerMain implements ZookeeperLaucher {

    public ZookeeperLaucher start(QuorumPeerConfig qConfig) throws ConfigException, IOException {
      ServerConfig config = new ServerConfig();
      config.readFrom(qConfig);;
      runFromConfig(config);
      return this;
    }

    public void shutdown() {
      super.shutdown();
    }
    
  }
}