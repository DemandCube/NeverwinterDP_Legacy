define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/api/UICallApi',
], function($, _, Backbone, ClusterGateway, UICollabsible, UICallApi) {

  var configs = [
    {
      label: "Server Ping",
      description: [
        "The ping method allow the client to check the running status of the servers ",
        "by the member name, member role.",
      ],
      syntax: "ClusterGateway.call('server', 'ping', {...})",
      sampleParams: [
        {
          description: "Check the status of member generic, expect one server status return",
          params: {"member-name": "generic"}
        },
        {
          description: "Check the status of member with the role generic, expect one or several status return",
          params: {"member-role": "generic"}
        },
      ],
      paramDescription: [
        { name: "member-name", description: "the member name in the cluster"},  
        { name: "member-role", description: "the member role in the cluster, a server member can have several role"}
      ],
      demos: [
        {
          name: "ping-all",
          description: "Ping all the servers",
          onRunDemo: function(thisUI) {
            console.log("On custom run demo") ;
            var result = ClusterGateway.call('server', 'ping', {}) ;
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
      syntax: "ClusterGateway.call('server', 'registration', {...})",
      sampleParams: [
        {
          description: "Get the server registration by member name or member name pattern",
          params: {"member-name": "generic"}
        },
        {
          description: "Get the server registration by member role or member role pattern",
          params: {"member-role": "generic"}
        },
      ],
      paramDescription: [
        { name: "member-name", description: "the member name in the cluster"},  
        { name: "member-role", description: "the member role in the cluster, a server member can have several role"}
      ],
      demos: [
        {
          name: "server-registration",
          description: "Get server registration of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('server', 'registration', {}) ;
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
      syntax: "ClusterGateway.call('server', 'metric', {...})",
      sampleParams: [
        {
          description: "Get the server registration by member name or member name pattern",
          params: {"member-name": "generic"}
        },
        {
          description: "Get the server registration by member role or member role pattern",
          params: {"member-role": "generic"}
        },
      ],
      paramDescription: [
        { name: "member-name", description: "the member name in the cluster"},  
        { name: "member-role", description: "the member role in the cluster, a server member can have several role"}
      ],
      demos: [
        {
          name: "server-metric",
          description: "Get server metric of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('server', 'metric', {}) ;
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
      syntax: "ClusterGateway.call('server', 'clearMetric', {...})",
      sampleParams: [
        {
          description: "Clear all the metric by member name or member name pattern",
          params: {"member-name": "generic", "expression": "*"}
        },
        {
          description: "Clear all the metric by member role or member role pattern",
          params: {"member-role": "generic", "expression": "*"}
        },
      ],
      paramDescription: [
        { name: "member-name", description: "the member name in the cluster"},  
        { name: "member-role", description: "the member role in the cluster, a server member can have several role"},
        { name: "expression",  dexpression: "the metric name pattern"}
      ],
      demos: [
        {
          name: "server-metric-clear",
          description: "Clear the server metric of all the servers",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('server', 'clearMetric', {"expression": "*"}) ;
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
      syntax: "ClusterGateway.call('server', 'jvminfo', {...})",
      sampleParams: [
        {
          description: "Get the server jvm info by member name or member name pattern",
          params: {"member-name": "generic"}
        },
        {
          description: "Get the server jvm info by member role or member role pattern",
          params: {"member-role": "generic"}
        },
      ],
      paramDescription: [
        { name: "member-name", description: "the member name in the cluster"},  
        { name: "member-role", description: "the member role in the cluster, a server member can have several role"}
      ],
      demos: [
        {
          name: "server-jvminfo",
          description: "Get server jvm info of the generic server",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('server', 'jvminfo', {"member-role": "generic"}) ;
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
