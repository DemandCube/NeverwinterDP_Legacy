define([
  'jquery',
  'underscore', 
  'backbone',
  'site/UIWorkspace',
  'text!plugins/ringbearer/api/UINavigation.jtpl'
], function($, _, Backbone, UIWorkspace, Template) {
  var UINavigation = Backbone.View.extend({

    initialize: function () {
      _.bindAll(this, 'render', 'onSelectUIComponent') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
    },

    events: {
      'click .onSelectUIComponent': 'onSelectUIComponent'
    },
    
    onSelectUIComponent: function(evt) {
      var name = $(evt.target).closest('.onSelectUIComponent').attr('name') ;
      console.log('on select: ' + name) ;

      require(['plugins/ringbearer/api/UI' + name + 'Api'], function(UIApi) { 
        UIWorkspace.setUIComponent(UIApi) ;
      }) ;
    }
  });

  return UINavigation ;
});
