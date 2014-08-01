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
      label: "Scheduler status",
      description: [
        "This method allow the client to list the status of the waitting jobs and running job."
      ],
      syntax: "ClusterGateway.execute('ringbearer scheduler [--param value]*')",

      commonParameters: CommonParameters.memberSelector,

      parameters: [ ],

      demos: [
        {
          name: "ringbearer-scheduler",
          description: "Get the service scheduler status",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('ringbearer scheduler --member-role ringbearer') ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    }
  ] ;

  var UIRingBearerApi = UICollabsible.extend({
    label: "RingBearer Api", 
    config: {
      actions: [ ]
    }
  }) ;
  
  
  var uiRingBearerApi = new UIRingBearerApi() ;
  for(var i = 0; i < configs.length; i++) {
    uiRingBearerApi.add(new UICallApi(configs[i])) ;
  }
  
  return uiRingBearerApi ;
});
