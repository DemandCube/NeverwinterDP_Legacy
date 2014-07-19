define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/api/UICallApi',
  'ui/api/CommonParameters',
], function($, _, Backbone, ClusterGateway, UICollabsible, UICallApi, CommonParameters) {

  var configs = [
    {
      label: "Server Ping",
      description: [
        "The ping method allow the client to check the running status of the servers ",
        "by the member name, member role.",
      ],
      syntax: "ClusterGateway.execute('server ping [--param-name param-value]')",
      
      commonParameters: CommonParameters.memberSelector,

      parameters: [ ],

      demos: [
        {
          name: "ping-all",
          description: "Ping all the servers",
          onRunDemo: function(thisUI) {
            console.log("On custom run demo") ;
            var result = ClusterGateway.execute('server ping') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    },

    {
      label: "Server Registration",
      description: [
        "The server registration command allow the client to get the full information(status, services...) of a server or a group of server"
      ],
      syntax: "ClusterGateway.execute('server registration [--param value]')",
      commonParameters: CommonParameters.memberSelector,
      parameters: [],
      demos: [
        {
          name: "server-registration",
          description: "Get server registration of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('server registration') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    },

    {
      label: "Server Metric",
      description: [
        "The server metric command allow the client to get the full metric information of a server or a group of server"
      ],
      syntax: "ClusterGateway.execute('server metric [--param value]')",
      commonParameters: CommonParameters.memberSelector,
      parameters: [
        { name: "--filter", description: "Filter the metric by the metric name", sample: "--filter *Kafka*" }
      ],
      demos: [
        {
          name: "server-metric",
          description: "Get server metric of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('server metric') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    },

    {
      label: "Server Metric Clear",
      description: [
        "The server metric clear command allow the client to clear the metric information of a server or a group of server"
      ],
      syntax: "ClusterGateway.execute('server metric-clear [--param value]')",

      commonParameters: CommonParameters.memberSelector,

      parameters: [
        { name: "--expression", description: "Select the metric name by the pattern expression", sample: "--expression *Kafka*" }
      ],

      demos: [
        {
          name: "server-metric-clear",
          description: "Clear the server metric of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('server metric-clear --expression *') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    },

    {
      label: "Server JVM Info",
      description: [
        "The server jvminfo command allow the client to get the full information of jvm(memory, thread, garbage collector)"
      ],
      syntax: "ClusterGateway.execute('server jvminfo [--param value]*')",

      commonParameters: CommonParameters.memberSelector,

      parameters: [ ],

      demos: [
        {
          name: "server-jvminfo",
          description: "Get server jvm info of the generic server",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('server jvminfo --member-role generic') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    }
  ] ;

  var UIServerApi = UICollabsible.extend({
    label: "Server Api", 
    config: {
      actions: [ ]
    }
  }) ;
  
  
  var uiServerApi = new UIServerApi() ;
  for(var i = 0; i < configs.length; i++) {
    uiServerApi.add(new UICallApi(configs[i]), true) ;
  }
  
  return uiServerApi ;
});
