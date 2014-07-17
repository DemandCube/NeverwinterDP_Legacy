define([
  'plugins/api/UINavigation'
], function(UINavigation) {
  var Plugin = {
    name: "api",
    label: "API",
    uiNavigation: new UINavigation()
  }

  return Plugin ;
});
