define([
  'plugins/demandspike/UINavigation',
  'plugins/demandspike/api/UINavigation'
], function(UINavigation, UIApiNavigation) {
  var Plugin = {
    name: "demandspike",
    label: "DemandSpike",
    uiNavigation: [new UINavigation(), new UIApiNavigation()]
  }

  return Plugin ;
});
