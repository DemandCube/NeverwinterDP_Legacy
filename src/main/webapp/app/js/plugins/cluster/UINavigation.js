define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UIBreadcumbs',
  'site/UIWorkspace',
  'plugins/cluster/UIListServer',
  'plugins/cluster/UIListModule',
  'plugins/cluster/UIClusterRegistration',
  'plugins/cluster/UIServerRegistration',
  'text!plugins/cluster/UINavigation.jtpl'
], function($, _, Backbone, ClusterGateway, UIBreadcumbs, UIWorkspace, UIListServer,
            UIListModule, UIClusterRegistration, UIServerRegistration, Template) {
  var UINavigation = Backbone.View.extend({

    initialize: function () {
      _.bindAll(
        this, 'render', 'onListServer', 'onListModule',
        'onServerRegistration', 'onClusterRegistration'
      ) ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var creg = ClusterGateway.getClusterRegistration() ;
      var params = { 
        clusterRegistration: ClusterGateway.getClusterRegistration()
      } ;
      $(this.el).html(this._template(params));
      $(this.el).find(".UINavigationMenu").menu();
    },

    events: {
      'click .onListServer': 'onListServer',
      'click .onListModule': 'onListModule',
      'click .onClusterRegistration': 'onClusterRegistration',
      'click .onServerRegistration': 'onServerRegistration'
    },
    
    onListServer: function(evt) {
      this._workspace(new UIListServer()) ;
    },

    onListModule: function(evt) {
      this._workspace(new UIListModule()) ;
    },

    onClusterRegistration: function(evt) {
      this._workspace(new UIClusterRegistration()) ;
    },

    onServerRegistration: function(evt) {
      var memberName = $(evt.target).attr("memberName") ;
      this._workspace(new UIServerRegistration({memberName: memberName})) ;
    },

    _workspace: function(uicomp) {
      var uiContainer = new UIBreadcumbs({el: null}) ;
      UIWorkspace.setUIComponent(uiContainer) ;
      uiContainer.add(uicomp) ;
    }
  });

  return UINavigation ;
});
