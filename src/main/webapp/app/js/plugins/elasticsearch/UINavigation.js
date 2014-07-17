define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'site/UIWorkspace',
  'text!plugins/elasticsearch/UINavigation.jtpl'
], function($, _, Backbone, ClusterGateway, UIWorkspace, Template) {
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
      require(['plugins/elasticsearch/' + name], function(UIDemo) { 
        UIWorkspace.setUIComponent(UIDemo) ;
      }) ;
    }

  });

  return UINavigation ;
});
