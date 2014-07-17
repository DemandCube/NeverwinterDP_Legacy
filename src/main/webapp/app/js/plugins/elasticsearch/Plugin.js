define([
  'plugins/elasticsearch/UINavigation'
], function(UINavigation) {
  var Plugin = {
    name: "elasticsearch",
    label: "Elasticsearch",
    uiNavigation: [ new UINavigation() ]
  }

  return Plugin ;
});
