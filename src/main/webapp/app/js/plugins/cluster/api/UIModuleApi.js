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
      label: "Module List",
      description: [
        "This method allow the client to list the avaialble and installed module and services on each server."
      ],
      syntax: "ClusterGateway.call('module', 'list', {...})",
      sampleParams: [
        {
          description: "List all the available modules",
          params: {"type": "available"}
        },
        {
          description: "List all the installed modules",
          params: {"type": "installed"}
        }
      ],
      paramDescription: [
      ],
      demos: [
        {
          name: "module-list-available",
          description: "List the avaialble modules",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('module', 'list', {"type": "available"}) ;
            thisUI.popupJSONResult(result) ;
          }
        },
        {
          name: "module-list-installed",
          description: "List the installed modules",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.call('module', 'list', {"type": "installed"}) ;
            thisUI.popupJSONResult(result) ;
          }
        }
      ]
    }
  ] ;

  var UIClusterApi = UICollabsible.extend({
    label: "Module Api", 
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
