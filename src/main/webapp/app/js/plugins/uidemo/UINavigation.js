define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'site/UIWorkspace',
  'text!plugins/uidemo/UINavigation.jtpl'
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
      console.log('on select: ' + name) ;

      require(['plugins/uidemo/' + name], function(UIDemo) { 
        UIWorkspace.setUIComponent(UIDemo) ;
      }) ;
    }

  });

  return UINavigation ;
});
