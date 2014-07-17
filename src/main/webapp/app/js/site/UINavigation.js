define([
  'jquery',
  'jqueryui',
  'underscore', 
  'backbone',
  'plugins/PluginManager',
  'text!site/UINavigation.jtpl'
], function($, jqueryui, _, Backbone, PluginManager, Template) {
  var UINavigation = Backbone.View.extend({
    el: $("#UINavigation"),
    
    initialize: function () {
      this.activePlugin = PluginManager.getPlugin('cluster') ;
      _.bindAll(this, 'render', 'onSelectPlugin') ;
    },
    
    _template: _.template(Template),

    _blockTmpl: _.template("<div style='padding: 10px' class='<%=name%>'></div>"),

    
    setActivePlugin: function(pluginName) {
      this.activePlugin = PluginManager.getPlugin(pluginName) ;
      this.render() ;
    },
    
    render: function() {
      var params = { 
        plugins: PluginManager.getPlugins()
      } ;
      $(this.el).html(this._template(params));
      var uiNavigation = this.activePlugin.uiNavigation ;
      for(var i = 0; i < uiNavigation.length; i++) {
        var cssClassName = 'ActivePluginNavigation' + i ;
        $(this.el).append(this._blockTmpl({name: cssClassName}));
        uiNavigation[i].setElement(this.$('.' + cssClassName)).render();
      }
    },

    events: {
      'click .onSelectPlugin': 'onSelectPlugin'
    },
    
    onSelectPlugin: function(evt) {
      var name = $(evt.target).attr('plugin') ;
      var plugin = app.view.UINavigation.setActivePlugin(name) ;
    }
  });
  
  return UINavigation ;
});
