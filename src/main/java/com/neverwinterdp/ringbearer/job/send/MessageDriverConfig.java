package com.neverwinterdp.ringbearer.job.send;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.neverwinterdp.yara.MetricRegistry;

public class MessageDriverConfig implements Serializable {
  @Parameter(
    names = "--driver", 
    description = "The message driver to send the message. Either dummy, kafka or sparkngin"
  )
  String driver = "dummy" ;
  
  @DynamicParameter(
      names = "--driver:", 
      description = "The message driver to send the message. Either dummy, kafka or sparkngin"
   )
   Map<String, String> driverProperties = new HashMap<String, String>() ;
  
  @Parameter(
    names = "--broker-connect", variableArity= true, 
    description = "The connection url list"
  )
  List<String> connect  = new ArrayList<String>() ;
  
  @Parameter(
    names = "--topic", description = "The destination topic of the message"
  )
  String topic  ;
  
  public String getDriver() { return this.driver ; }
  
  public MessageDriver createDriver(MetricRegistry mRegistry) {
    MessageDriver mdriver = null ;
    if("kafka".equals(driver)) {
      mdriver = new KafkaMessageDriver(mRegistry) ;
    } else if("sparkngin".equals(driver)) {
        mdriver = new HttpSparknginMessageDriver(mRegistry) ;
    } else {
      mdriver = new DummyMessageDriver(mRegistry) ;
    }
    mdriver.init(driverProperties, connect, topic);
    return mdriver ;
  }
}