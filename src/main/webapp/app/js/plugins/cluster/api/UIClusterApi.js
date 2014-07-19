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
      label: "Cluster Registration",
      description: [
        "This method allow the client to load the entire cluster information, the info, services, status of each server."
      ],
      syntax: "ClusterGateway.execute('cluster registration')",
      sampleParams: [
        {
          description: "No parameter is required",
          params: {}
        }
      ],
      paramDescription: [
      ],
      demos: [
        {
          name: "cluster-registration",
          description: "Load the cluster registration",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute("cluster registration") ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    }
  ] ;

  var UIClusterApi = UICollabsible.extend({
    label: "Cluster Api", 
    config: {
      actions: [ ]
    }
  }) ;
  
  
  var uiClusterApi = new UIClusterApi() ;
  for(var i = 0; i < configs.length; i++) {
    uiClusterApi.add(new UICallApi(configs[i])) ;
  }
  
  return uiClusterApi ;
});
