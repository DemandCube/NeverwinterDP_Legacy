define([
  'plugins/ringbearer/UINavigation',
  'plugins/ringbearer/api/UINavigation'
], function(UINavigation, UIApiNavigation) {
  var Plugin = {
    name: "ringbearer",
    label: "RingBearer",
    uiNavigation: [new UINavigation(), new UIApiNavigation()]
  }

  return Plugin ;
});
