define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UIUtil',
  'text!ui/UIBreadcumbs.jtpl'
], function($, _, Backbone, UIUtil, UIBreadcumbsTmpl) {
  var UIBreadcumbs = Backbone.View.extend({
    
    initialize: function (options) {
      this.el = options.el ;
      this.type = 'UIBreadcumbs' ;
      this.views = [] ;
      
      _.bindAll(this, 'render', 'onSelectView') ;
      this.renderTmpl() ;
    },
    
    _template: _.template(UIBreadcumbsTmpl),
    
    renderTmpl: function() {
      var params = {} ;
      $(this.el).html(this._template(params));
    },
    
    render: function() {
      this.renderTmpl() ;
      var view = this.views.pop() ;
      if(view != null) view.render() ;
    },
    
    _buttonTmpl: _.template(
      "<a class='onSelectView ui-action ui-disabled'><%=label%></a>"
    ),

    add: function(uicomponent) {
      this.push(uicomponent) ;
    },

    remove: function(uicomponent) {
      throw new Error('to implement') ;
    },
    
    push: function(view) {
      view.uiParent = this ;
      this.views.push(view) ;
      var label = view.label ;
      if(label == null) label = "???" ;
      var breadcumbs = this.$('.Breadcumbs') ;
      if(this.views.length > 1) {
        breadcumbs.append("<span style='font-weight: bold'> &gt;&gt; </span>");

      }
      breadcumbs.find("a").removeClass('ui-disabled');
      breadcumbs.append(this._buttonTmpl({label: label}));

      this.$('.BreadcumbsView').unbind() ;
      view.UIParent = this ;
      view.setElement(this.$('.BreadcumbsView')).render();
    },
    
    back: function() {
      if(this.views.length <= 1) return ;
      var view = this.views[this.views.length - 2];
      this._removeToLabel(view.label) ;
    },

    getAncestorOfType: function(type) {
      return UIUtil.getValidatorOfType(this, type) ;
    },
    
    events: {
      'click a.onSelectView': 'onSelectView'
    },
    
    onSelectView: function(evt) {
      var label = $.trim($(evt.target).text()) ;
      this._removeToLabel(label);
    },


    _removeToLabel: function(label) {
      var breadcumbs = this.$('.Breadcumbs') ;
      for(var i = this.views.length - 1; i >= 0; i--) {
        if(this.views[i].label == label) {
          this.$('.BreadcumbsView').unbind() ;
          breadcumbs.find("a:last-child").addClass("ui-disabled");
          this.views[i].setElement(this.$('.BreadcumbsView')).render();
          return ;
        } else {
          var view = this.views.pop() ;
          breadcumbs.find("a:last-child").remove();
          breadcumbs.find("span:last-child").remove();
        }
      }
    }
  });
  
  return UIBreadcumbs ;
});
