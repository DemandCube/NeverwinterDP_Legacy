define([
  'jquery',
  'underscore', 
  'backbone',
  'text!site/UIBanner.jtpl'
], function($, _, Backbone, BannerTmpl) {
  var UIBanner = Backbone.View.extend({
    el: $("#UIBanner"),
    
    initialize: function () {
      _.bindAll(this, 'render') ;
    },
    
    _template: _.template(BannerTmpl),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
    }
  });
  
  return UIBanner ;
});
