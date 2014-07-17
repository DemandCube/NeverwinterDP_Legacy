define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UIUtil',
  'text!ui/UICollapsible.jtpl',
], function($, _, Backbone, UIUtil, UICollapsibleTmpl) {
  /**
   *@type ui.UICollapsible 
   */
  var UICollapsible = Backbone.View.extend({
    
    initialize: function (options) {
      this.clear() ;
      this.type = 'UICollapsible' ;
      this.onInit(options) ;
      _.bindAll(this, 'render', 'onToggleBlock', 'onAction') ;
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
    
    setActionHidden: function(actionName, bool) {
      if(this.state.actions[actionName] == null) {
        this.state.actions[actionName] = {} ;
      }
      this.state.actions[actionName].hidden = bool ;
    },
    
    _template: _.template(UICollapsibleTmpl),
    
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
        var blockClass = '.UICollapsibleBlockContent' + i ;
        comp.setElement(this.$(blockClass)).render();
      }
    },
    
    events: {
      'click a.onToggleBlock': 'onToggleBlock',
      'click a.onAction': 'onAction',
    },
    
    onToggleBlock: function(evt) {
      var compIdx = $(evt.target).closest("a").attr("component") ;
      this.components[compIdx].collapible.collapsed = !this.components[compIdx].collapible.collapsed ;
      var blockClass = '.UICollapsibleBlockContent' + compIdx ;
      var iconEle =$(evt.target).closest(".ui-icon");
      if(this.components[compIdx].collapible.collapsed) {
        iconEle.removeClass("ui-icon-minus");
        iconEle.addClass("ui-icon-plus");
        this.$(blockClass).css("display", "none");
      } else {
        iconEle.removeClass("ui-icon-plus");
        iconEle.addClass("ui-icon-minus");
        this.$(blockClass).css("display", "block");
      }
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
  
  return UICollapsible ;
});
