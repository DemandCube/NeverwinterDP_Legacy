define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UIBreadcumbs',
  'site/UIWorkspace',
  'plugins/elasticsearch/ESGateway',
  'plugins/elasticsearch/UINodes',
  'plugins/elasticsearch/UIIndices',
  'text!plugins/elasticsearch/UINavigation.jtpl'
], function($, _, Backbone, ClusterGateway, UIBreadcumbs, UIWorkspace, ESGateway, UINodes, UIIndices, Template) {
  var UINavigation = Backbone.View.extend({

    initialize: function () {
      _.bindAll(this, 'render') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create") ;
    },

    events: {
      'click .onSelectCluster': 'onSelectCluster',
      'click .onSelectNodes': 'onSelectNodes',
      'click .onSelectIndices': 'onSelectIndices',
      'click .onSelectStats': 'onSelectStats'
    },

    onSelectCluster: function(evt) {
      console.log('on select cluster...') ;
      console.printJSON(ESGateway.cluster.stats()) ;
    },

    onSelectNodes: function(evt) {
      console.log('on select nodes...') ;
      this._workspace(new UINodes({})) ;
    },

    onSelectIndices: function(evt) {
      this._workspace(new UIIndices({})) ;
    },

    onSelectStats: function(evt) {
      console.log('on select stats...') ;
      console.printJSON(ESGateway.stats.all()) ;
    },

    _workspace: function(uicomp) {
      var uiContainer = new UIBreadcumbs({el: null}) ;
      UIWorkspace.setUIComponent(uiContainer) ;
      uiContainer.add(uicomp) ;
    }
  });

  return UINavigation ;
});
