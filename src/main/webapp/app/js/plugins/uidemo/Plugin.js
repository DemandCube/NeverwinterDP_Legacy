define([
  'plugins/uidemo/UINavigation'
], function(UINavigation) {
  var Plugin = {
    name: "uidemo",
    label: "UI Demo",
    uiNavigation: [ new UINavigation()]
  }

  return Plugin ;
});
