define([
  'jquery',
  'underscore', 
  'backbone',
  'text!site/UIWorkspace.jtpl'
], function($, _, Backbone, WorkspaceTmpl) {
  var UIWorkspace = Backbone.View.extend({
    el: $("#UIWorkspace"),
    
    initialize: function () {
      _.bindAll(this, 'render') ;
    },
    
    _template: _.template(WorkspaceTmpl),
    
    render: function() {
      var params = { 
      } ;
      $(this.el).html(this._template(params));
    },
    
    setUIComponent: function(uicomp) {
      this.uicomponent  = uicomp ;
      $(this.el).empty();
      $(this.el).unbind();
      uicomp.setElement($('#UIWorkspace')).render();
    }
  });
  
  return new UIWorkspace() ;
});
