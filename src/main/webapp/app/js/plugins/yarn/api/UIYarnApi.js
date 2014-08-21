define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/Server',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/api/UICallApi',
  'ui/api/CommonParameters',
], function($, _, Backbone, Server, ClusterGateway, UICollabsible, UICallApi, CommonParameters) {

  var configs = [
    {
      label: "Application History List",
      description: [
        "This method allow to list all the running and finished yarn application that use NeverwinterDP yarn framework"
      ],
      syntax: "http://host:port/yarn-app/history/list",

      commonParameters: [],

      parameters: [ ],

      demos: [
        {
          name: "yarn-application-history-list",
          description: "List the yarn applications",
          onRunDemo: function(thisUI) {
            var result = Server.syncGETResource("/yarn-app/history/list", "json") ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    },

    {
      label: "Application History Info",
      description: [
        "This method allow to get the info of a running or finished yarn application that use NeverwinterDP yarn framework"
      ],
      syntax: "http://host:port/yarn-app/history/app/:appId",

      commonParameters: [],

      parameters: [ ],

      demos: [
        {
          name: "yarn-application-history-info",
          description: "Get the yarn application info",
          onRunDemo: function(thisUI) {
            var list = Server.syncGETResource("/yarn-app/history/list", "json") ;
            if(list.length > 0) {
              var result = Server.syncGETResource("/yarn-app/history/app/" + list[0].appId, "json") ;
              thisUI.popupJSONResult(result) ;
            }
          }
        }
      ]
    }
  ] ;

  var UIDemandSpikeApi = UICollabsible.extend({
    label: "DemandSpike Api", 
    config: {
      actions: [ ]
    }
  }) ;
  
  
  var uiDemandSpikeApi = new UIDemandSpikeApi() ;
  for(var i = 0; i < configs.length; i++) {
    uiDemandSpikeApi.add(new UICallApi(configs[i])) ;
  }
  
  return uiDemandSpikeApi ;
});
