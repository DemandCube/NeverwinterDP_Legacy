define([
  'plugins/cluster/UINavigation',
  'plugins/cluster/api/UINavigation'
], function(UINavigation, UIApiNavigation) {
  var Plugin = {
    name: "cluster",
    label: "Cluster",
    uiNavigation: [ new UINavigation(), new UIApiNavigation() ]
  }

  return Plugin ;
});
