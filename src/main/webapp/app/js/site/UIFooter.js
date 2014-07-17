define([
  'jquery',
  'underscore', 
  'backbone',
  'text!site/UIFooter.jtpl'
], function($, _, Backbone, FooterTmpl) {
  var UIFooter = Backbone.View.extend({
    el: $("#UIFooter"),
    
    initialize: function () {
      _.bindAll(this, 'render') ;
    },
    
    _template: _.template(FooterTmpl),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create") ;
    },
    
    events: {
      'change select.onSelectLanguage': 'onSelectLanguage'
    },
    
    onSelectLanguage: function(evt) {
    }
  });
  
  return UIFooter ;
});
