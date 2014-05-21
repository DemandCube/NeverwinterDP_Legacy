package com.neverwinterdp.server.command;

import com.neverwinterdp.server.Server;
import com.neverwinterdp.server.service.Service;
import com.neverwinterdp.server.service.ServiceRegistration;
import com.neverwinterdp.server.service.ServiceState;

public class ServiceCommands {
  static public class Ping extends ServiceCommand<ServiceState> {
    public ServiceState execute(Server server, Service service) throws Exception {
      return service.getServiceRegistration().getState() ;
    }
  }
  
  static public class Start extends ServiceCommand<ServiceRegistration> {
    public ServiceRegistration execute(Server server, Service service) throws Exception {
      ServiceRegistration registration = service.getServiceRegistration() ;
      server.getServiceContainer().start(registration);
      return service.getServiceRegistration() ;
    }
  }

  static public class Stop extends ServiceCommand<ServiceRegistration> {
    public ServiceRegistration execute(Server server, Service service) throws Exception {
      ServiceRegistration registration = service.getServiceRegistration() ;
      server.getServiceContainer().stop(registration);
      return service.getServiceRegistration() ;
    }
  }
}
