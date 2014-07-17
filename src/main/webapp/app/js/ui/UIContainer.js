define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UIUtil',
  'text!ui/UIContainer.jtpl',
], function($, _, Backbone, UIUtil, UIContainerTmpl) {
  /**
   *@type ui.UIContainer 
   */
  var UIContainer = Backbone.View.extend({
    
    initialize: function (options) {
      this.clear() ;
      this.type = 'UIContainer' ;
      this.onInit(options) ;
      _.bindAll(this, 'render', 'onAction') ;
    },
    
    onInit: function(options) { },

    
    add: function(component, collapsed) {
      component.collapible = {} ;
      if(collapsed) component.collapible.collapsed = true ;
      component.uiParent = this ;
      this.components.push(component) ; 
    },

    remove: function(component) {
      var holder = [] ;
      for(var i = 0; i < this.components.length; i++) {
        if(this.components[i] !=  component) {
          holder.push(this.components[i]) ;
        }
      }
      this.components = holder() ;
    },

    clear: function() { 
      this.components = [] ;
      this.state = { actions: {} } ;
    },

    getAncestorOfType: function(type) {
      return UIUtil.getAncestorOfType(this, type) ;
    },

    setHideHeader: function(bool) {
      this.state.hideHeader = bool ; 
    },

    setHideFooter: function(bool) {
      this.state.hideFooter = bool ; 
    },
    
    setActionHidden: function(actionName, bool) {
      if(this.state.actions[actionName] == null) {
        this.state.actions[actionName] = {} ;
      }
      this.state.actions[actionName].hidden = bool ;
    },
    
    _template: _.template(UIContainerTmpl),
    
    render: function() {
      var params = {
        title:      this.label,
        config:     this.config,
        state:      this.state,
        components: this.components
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create");
      
      for(var i = 0; i < this.components.length; i++) {
        var comp = this.components[i] ;
        var blockClass = '.UIContainerBlockContent' + i ;
        comp.setElement(this.$(blockClass)).render();
      }
    },
    
    events: {
      'click a.onAction': 'onAction'
    },
    
    onAction: function(evt) {
      var actionName= $(evt.target).closest('a').attr('action') ;
      var actions = this.config.actions ;
      for(var i = 0; i < actions.length; i++) {
        if(actions[i].action == actionName) {
          actions[i].onClick(this) ;
          return ;
        }
      }
    }
  });
  
  return UIContainer ;
});
