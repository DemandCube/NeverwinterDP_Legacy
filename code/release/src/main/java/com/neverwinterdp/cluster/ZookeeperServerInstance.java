package com.neverwinterdp.cluster;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

public class ZookeeperServerInstance {
  private int id ;
  private String dataDir ;
  private int clientPort ;
  private ZooKeeperServer server ;
  
  public ZookeeperServerInstance(int id, String dataDir, int clientPort) throws Exception {
    this.id = id ;
    this.dataDir = dataDir ;
    this.clientPort = clientPort ;
  }
  
  public String getDataDir() { return this.dataDir ; }
  
  public int getClientPort() { return this.clientPort ; }
  
  public String getConnectURL() { return "127.0.0.1:" + clientPort ; }
  
  public void startup() throws Exception {
    if(server != null) {
      throw new Exception("The server is already started!") ;
    }
    
    int numConnections = 5000;
    int tickTime = 2000;
    
    File dir = new File(dataDir, "data").getAbsoluteFile();
    server = new ZooKeeperServer(dir, dir, tickTime);
    ServerCnxnFactory factory = ServerCnxnFactory.createFactory();
    factory.configure(new InetSocketAddress(clientPort), numConnections);
    factory.startup(server); // start the server.
    System.out.println("Launched Zookeeper Server " + id);
  }
  
  public void shutdown() throws IOException {
    server.getServerCnxnFactory().closeAll() ;
    server.getTxnLogFactory().close() ;
    server.getZKDatabase().close() ;
    server.shutdown() ;
    System.out.println("Shutdown Zookeeper Server " + id);
  }
}