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
      label: "Status",
      description: [
        "This method allow the client to list the status of the waitting jobs and running job."
      ],
      syntax: "ClusterGateway.execute('demandspike status [--param value]*')",

      commonParameters: CommonParameters.memberSelector,

      parameters: [ ],

      demos: [
        {
          name: "demandspike-status",
          description: "Get the service scheduler status",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('demandspike status --member-role demandspike') ;
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
