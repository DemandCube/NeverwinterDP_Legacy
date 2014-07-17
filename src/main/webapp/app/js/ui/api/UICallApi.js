define([
  'jquery',
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UIPopup',
  'ui/UIContent',
  'text!ui/api/UICallApi.jtpl',
  'css!ui/api/UICallApi.css'
], function($, _, Backbone, ClusterGateway, UIPopup, UIContent, Template) {
  var UICallApi = Backbone.View.extend({

    initialize: function (config) {
     this.label = config.label ;
     this.config = config ;

      _.bindAll(this, 'render', 'onRunDemo') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { config: this.config } ;
      $(this.el).html(this._template(params));
    },

    events: {
      'click .onRunDemo': 'onRunDemo'
    },
    
    onRunDemo: function(evt) {
      var name = $(evt.target).attr('name') ;
      for(var i = 0; i < this.config.demos.length; i++) {
        var demo = this.config.demos[i] ;
        if(name == demo.name) {
          demo.onRunDemo(this) ;
          return ;
        }
      }
    },

    popupJSONResult: function(result) {
      var json = JSON.stringify(result, null, "  ") ;
      var popupConfig = {
        title: "Return JSON", 
        minWidth: 800, 
        maxHeight: 600, 
        modal: true,
        highlightSyntax: 'json'

      } ;
      var uiContent = new UIContent({content: json}) ;
      UIPopup.activate(uiContent, popupConfig) ;
    }
  });

  return UICallApi ;
});
