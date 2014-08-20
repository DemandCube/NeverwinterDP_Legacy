define([
  'plugins/yarn/UINavigation',
  'plugins/yarn/api/UINavigation'
], function(UINavigation, UIApiNavigation) {
  var Plugin = {
    name: "yarn",
    label: "Yarn Application",
    uiNavigation: [new UINavigation(), new UIApiNavigation()]
  }

  return Plugin ;
});
