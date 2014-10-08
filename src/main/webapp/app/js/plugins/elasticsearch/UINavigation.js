define([
  'jquery',
  'underscore', 
  'backbone',
  'ui/UIBreadcumbs',
  'site/UIWorkspace',
  'plugins/elasticsearch/ESGateway',
  'plugins/elasticsearch/UINodes',
  'plugins/elasticsearch/UIIndices',
  'plugins/elasticsearch/UISearch',
  'text!plugins/elasticsearch/UINavigation.jtpl'
], function($, _, Backbone, UIBreadcumbs, UIWorkspace, ESGateway, UINodes, UIIndices, UISearch, Template) {
  var UINavigation = Backbone.View.extend({

    initialize: function () {
      _.bindAll(this, 'render') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { 
         indices: ESGateway.index.available()
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create") ;
      this.onSelectNodes(null) ;
    },

    events: {
      'click .onSelectCluster': 'onSelectCluster',
      'click .onSelectNodes': 'onSelectNodes',
      'click .onSelectIndices': 'onSelectIndices',
      'click .onSelectSearchIndex': 'onSelectSearchIndex',
      'click .onSelectStats': 'onSelectStats'
    },

    onSelectCluster: function(evt) {
      console.log('on select cluster...') ;
      console.printJSON(ESGateway.cluster.stats()) ;
    },

    onSelectNodes: function(evt) {
      this._workspace(new UINodes({})) ;
    },

    onSelectIndices: function(evt) {
      this._workspace(new UIIndices({})) ;
    },

    onSelectSearchIndex: function(evt) {
      var index = $(evt.target).attr('name') ;
      this._workspace(new UISearch({index: index})) ;
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
