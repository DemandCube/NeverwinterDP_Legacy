define([
  'jquery', 
  'underscore', 
  'backbone',
  'text!ui/UITableTree.jtpl',
  'css!ui/UITableTree.css'
], function($, _, Backbone, UITableTreeTmpl) {

  var Node = function(id, bean) {
    this.id = id ;
    this.bean = bean ;
    this.children = [] ;
    this.collapse = true ;

    this.addChild = function(bean) {
      var node = new Node(this.id + '-' + this.children.length, bean) ;
      this.children.push(node) ;
      return node ;
    },

    this.findNode = function(nodeId) {
      if(nodeId == this.id) return this ;
      for(var i = 0; i < this.children.length; i++) {
        var node = this.children[i].findNode(nodeId) ;
        if(node != null) return node ;
      }
      return null ;
    },

    this.setCollapse = function(nodeId) {
      this.collapse = collapse ;
    }
  };

  var UITableTree = Backbone.View.extend({
    initialize: function (options) {
      this.nodes = [] ;
      if(this.onInit) {
        this.onInit(options) ;
      }
      _.bindAll(this, 'render') ;
    },

    addNode: function(bean) {
      var node = new Node('id-' + this.nodes.length, bean) ;
      this.nodes.push(node) ;
      return node ;
    },
    
    _template: _.template(UITableTreeTmpl),

    render: function() {
      var params = {
        config: this.config,
        nodes:  this.nodes
      };

      $(this.el).html(this._template(params));

      $(this.el).find('table').on('click', '.toggle', function () {
        //Gets all <tr>'s  of greater depth
        //below element in the table
        var findChildren = function (tr) {
          var depth = tr.data('depth');
          return tr.nextUntil($('tr').filter(function () {
            return $(this).data('depth') <= depth;
          }));
        };

        var el = $(this);
        var tr = el.closest('tr'); //Get <tr> parent of toggle button
        var children = findChildren(tr);

        //Remove already collapsed nodes from children so that we don't
        //make them visible. 
        //(Confused? Remove this code and close Item 2, close Item 1 
        //then open Item 1 again, then you will understand)
        var subnodes = children.filter('.expand');
        subnodes.each(function () {
          var subnode = $(this);
          var subnodeChildren = findChildren(subnode);
          children = children.not(subnodeChildren);
        });

        //Change icon and hide/show children
        if(tr.hasClass('collapse')) {
          tr.removeClass('collapse').addClass('expand');
          children.hide();
        } else {
          tr.removeClass('expand').addClass('collapse');
          children.show();
        }
        return children;
      });
    },

    events: {
      'click  a.onBeanFieldClick': 'onBeanFieldClick',
    },

    onBeanFieldClick: function(evt) {
      var eleA = $(evt.target).closest('a') ;
      var nodeId = eleA.attr('nodeId') ;
      var fieldIdx = eleA.attr('field') ;
      var field = this.config.bean.fields[fieldIdx].field ;
      var node = this._findNode(nodeId) ;
      console.log('on click node ' + node.id + ', field = ' + field) ;
    },

    _findNode: function(nodeId) {
      for(var i = 0; i < this.nodes.length; i++) {
        var node = this.nodes[i].findNode(nodeId)
        if(node != null) return node ;
      }
      return null ;
    }
  
  });
  
  return UITableTree ;
});
