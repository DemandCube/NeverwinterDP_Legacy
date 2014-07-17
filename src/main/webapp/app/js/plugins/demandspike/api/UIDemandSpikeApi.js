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
      label: "Status",
      description: [
        "This method allow the client to list the status of the waitting jobs and running job."
      ],
      syntax: "ClusterGateway.call('demandspike', 'status', {...})",
      sampleParams: [
        {
          description: "No params is required",
          params: {}
        }
      ],
      paramDescription: [
      ],
      demos: [
        {
          name: "demandspike-status",
          description: "Get the service scheduler status",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('demandspike', 'status', {"member-role": "demandspike"}) ;
            thisUI.popupJSONResult(result) ;
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
