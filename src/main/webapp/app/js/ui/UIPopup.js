define([
  'jquery', 
  'underscore', 
  'backbone'
], function($, _, Backbone) {
  var UIPopup = Backbone.View.extend({
    el: "#UIPopupDialog",
    
    initialize: function (config) {
      this.type = 'UIPopup' ;
    },

    activate: function(uicomp, config) {
      uicomp.uiParent = this ;
      uicomp.setElement($(this.el)).render();
      var thisUI = this ;
      config.close = function() {
        thisUI.closePopup() ;
      };
      $(this.el).dialog(config);
    },
    
    closePopup: function() {
      $(this.el).html("") ;
      $(this.el).unbind()  ;
      $(this.el).dialog("destroy");
    }
  });
  
  return new UIPopup() ;
});
