define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/api/UICallApi',
  'ui/api/CommonParameters',
], function($, _, Backbone, ClusterGateway, UICollabsible, UICallApi, CommonParameters) {
  var commonParameters = [
    { name: "--member-role", description: "Select the target member by role", sample: "--membe-role generic" },
    { name: "--member-name", description: "Select the target member by member name", sample: "--member-name generic" },
    { name: "--member-uuid", description: "Select the target member by member uuid" },
  ];

  var configs = [
    {
      label: "Module List",
      description: [
        "This method allow the client to list the avaialble and installed module and services on each server."
      ],
      syntax: "ClusterGateway.execute('module list [--param value]*')",

      commonParameters: CommonParameters.memberSelector,

      parameters: [
        { name: "--type", defaultValue: "available", description: "Status type of the module , can be available or installed", sample: "--type installed" }
      ],

      demos: [
        {
          name: "module-list-available",
          description: "List the avaialble modules",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('module list --type available') ;
            thisUI.popupJSONResult(result) ;
          }
        },
        {
          name: "module-list-installed",
          description: "List the installed modules",
          onRunDemo: function(thisUI) {
            var result = ClusterGateway.execute('module list --type installed') ;
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
