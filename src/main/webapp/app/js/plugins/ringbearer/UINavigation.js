define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UIBreadcumbs',
  'site/UIWorkspace',
  'text!plugins/ringbearer/UINavigation.jtpl'
], function($, _, Backbone, ClusterGateway, UIBreadcumbs, UIWorkspace, Template) {
  var UINavigation = Backbone.View.extend({

    initialize: function () {
      _.bindAll(this, 'render', 'onSelectUIComponent') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create") ;
    },

    events: {
      'click .onSelectUIComponent': 'onSelectUIComponent'
    },
    
    onSelectUIComponent: function(evt) {
      var name = $(evt.target).closest('.onSelectUIComponent').attr('name') ;
      require(['plugins/ringbearer/' + name], function(UIComponent) { 
        var uiContainer = new UIBreadcumbs({el: null}) ;
        UIWorkspace.setUIComponent(uiContainer) ;
        uiContainer.add(new UIComponent()) ;
      }) ;
    },
  });

  return UINavigation ;
});
