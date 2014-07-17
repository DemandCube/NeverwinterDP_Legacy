define([
  'jquery', 
  'underscore', 
  'backbone',
  'util/PageList',
  'ui/UIUtil',
  'ui/UIPopup',
  'ui/UIBean',
  'text!ui/UITable.jtpl',
  'text!ui/UITableRows.jtpl',
  'css!ui/UITable.css'
], function($, _, Backbone, PageList, UIUtil, UIPopup, UIBean, UITableTmpl, UITableRowsTmpl) {
  var UITableBean = UIBean.extend({
    config: {
      beans: {
        bean: {
          name: 'bean', label: 'Bean',
          edit: {
            disable: false,
            actions: [
              {
                action:'save', label: "Save", icon: "check",
                onClick: function(thisUI, beanConfig, beanState) { 
                  if(thisUI.row >= 0) {
                    thisUI.UITable.markModifiedItemOnCurrentPage(thisUI.row) ;
                  } else {
                    thisUI.UITable.addBean(thisUI.getBean('bean')) ;
                  }
                  thisUI.UITable.renderRows() ;
                }
              },
              {
                action:'cancel', label: "Cancel", icon: "back",
                onClick: function(thisUI, beanConfig, beanState) { 
                }
              }
            ],
          }
        }
      }
    },
    
    init: function(UITable, bean, row) {
      this.UITable = UITable ;
      this.row = row ;
      var isNew = row < 0 ;
      if(isNew) this.label = 'New ' + UITable.config.bean.label   ;
      else this.label      = 'Edit ' + UITable.config.bean.label   ;
      this.config.beans.bean.fields = UITable.config.bean.fields ;
      this.bind('bean', bean, isNew) ;
      this.getBeanState('bean').editMode = true ;
      return this ;
    }
  });
  
  /**
   *@type ui.UITable 
   */
  var UITable = Backbone.View.extend({
    initialize: function (options) {
      this.tableId = this.randomId() ;
      if(this.onInit) {
        this.onInit(options) ;
      }
      _.bindAll(this, 'render', 'onSwitchToolbar',
                'onToggleColumnSelector', 'onToggleColumn', 'onSelectDisplayRow', 
                'onDfltToolbarAction', 'onDfltBeanFilter',
                'onFilterMoreOption', 'onFilter', 
                'onSelectPage') ;
    },
    
    _template: _.template(UITableTmpl),
    
    _rows: _.template(UITableRowsTmpl),
    
    /**
     *@memberOf ui.UITable 
     */
    setBeans: function(beans) {
      this.beans = beans ;
      var pageSize = this.config.pageSize ;
      if(pageSize == null) pageSize = 10 ;
      this.state = {beans: [], pageSize: pageSize } ;
      for(var i = 0; i < beans.length; i++) {
        this.state.beans.push( { bean: beans[i] }) ;
      }
      this._filter() ;
    },
    
    getBeans: function() {
      this.commitChange() ;
      return this.beans ; 
    },
    
    commitChange: function() {
      this.beans.length = 0 ;
      for(var i = 0; i < this.state.beans.length; i++) {
        this.beans[i] = this.state.beans[i].bean ;
      }
    },
    
    /**
     *@memberOf ui.UITable
     */
    getItemOnCurrentPage : function(idx) {
      return this.pageList.getItemOnCurrentPage(idx).bean ;
    },
    
    /**
    -*@memberOf ui.UITable
     */
    removeItemOnCurrentPage : function(idx) {
      var item = this.pageList.getItemOnCurrentPage(idx) ;
      this.pageList.removeItemOnCurrentPage(idx) ;
      var index = this.state.beans.indexOf(item);
      if(index >= 0) {
        this.state.beans.splice(index, 1) ;
      }
      this.renderRows() ;
    },

    /**
     *@memberOf ui.UITable 
     */
    addBean: function(bean) {
      if(this.state == null) {
        var pageSize = this.config.pageSize ;
        if(pageSize == null) pageSize = 10 ;
        this.state = {beans: [], pageSize: pageSize } ;
      }
      bean.persistableState = 'NEW' ;
      this.state.beans.push({ bean: bean }) ;
      this._updateBeanStateCount() ;
      this._filter() ;
    },
    
    markDeletedItemOnCurrentPage : function(idx) {
      var item = this.pageList.getItemOnCurrentPage(idx) ;
      item.bean.persistableState = 'DELETED' ;
      this._updateBeanStateCount() ;
      this.renderRows() ;
    },
    
    markModifiedItemOnCurrentPage : function(idx) {
      var item = this.pageList.getItemOnCurrentPage(idx) ;
      item.bean.persistableState = 'MODIFIED' ;
      this._updateBeanStateCount() ;
      this.renderRows() ;
    },

    getAncestorOfType: function(type) {
      return UIUtil.getAncestorOfType(this, type) ;
    },
    
    /**
     *@memberOf ui.UITable 
     */
    render: function() {
      var params = {
        config:   this.config,
        pageList: this.pageList,
        tableId: this.tableId
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create");
      this.renderRows() ;
    },
    
    renderRows: function() {
      var params = { 
        config:   this.config,
        state: this.state,
        pageList: this.pageList,
        tableId: this.tableId 
      } ;
      var tableBlock = $(this.el).find(".UITableRows");
      tableBlock.html(this._rows(params));
      tableBlock.trigger("create");
    },
    
    events: {
      'click  a.onSwitchToolbar': 'onSwitchToolbar',
      
      'click  a.onToggleColumnSelector': 'onToggleColumnSelector',
      'change select.onSelectDisplayRow': 'onSelectDisplayRow',
      'change input.onToggleColumn': 'onToggleColumn',
      'click  a.onDfltToolbarAction': 'onDfltToolbarAction',
      'keyup  .onDfltBeanFilter': 'onDfltBeanFilter',
      'change .onDfltBeanFilter': 'onDfltBeanFilter',
      'blur   .onDfltBeanFilter': 'onDfltBeanFilter',
      
      'click  a.onFilterMoreOption': 'onFilterMoreOption',
      'click  a.onFilter': 'onFilter',
      
      'click  a.onSearch': 'onSearch',
      
      'click  a.onSelectPage': 'onSelectPage',
      
      'click  a.onBeanFieldClick': 'onBeanFieldClick',
      'click  a.onAddBean': 'onAddBean',
      'click  a.onBeanAction': 'onBeanAction'
    },
    
    onSwitchToolbar: function(evt) {
      var toolbars = [$(this.el).find('div.UITableDefaultToolbar')] ;
      if(this.config.toolbar.search != null) {
        toolbars.push($(this.el).find('div.UITableSearchToolbar')) ;
      }
      if(this.config.toolbar.filter != null) {
        toolbars.push($(this.el).find('div.UITableFilterToolbar')) ;
      }
      
      for(var i = 0; i < toolbars.length; i++) {
        var toolbar = toolbars[i] ;
        if(toolbar.css('display') == 'block') {
          toolbar.css('display', 'none') ;
          if(i + 1 < toolbars.length) toolbars[i + 1].css('display', 'block') ;
          else toolbars[0].css('display', 'block') ;
          return ;
        }
      }
    },
    
    onToggleColumnSelector: function(evt) {
      var toolbar = $(evt.target).closest('div.UITableDefaultToolbar') ;
      this._toogleBlock(toolbar.find('div.ColumnSelector')) ;
    },
    
    onToggleColumn: function(evt) {
      var fieldName = $(evt.target).attr('name') ;
      var fields = this.config.bean.fields ;
      for(var i = 0; i < fields.length; i++) {
        var fieldConfig = fields[i] ;
        if(fieldName == fieldConfig.field) {
          fieldConfig.toggled = !fieldConfig.toggled ;
          this.renderRows() ;
          return ;
        }
      }
    },
    
    onSelectDisplayRow: function(evt) {
      var pageSize = $(evt.target, ".onSelectDisplayRow").find(":selected").attr("value") ;
      this.config.pageSize = pageSize ;
      this.pageList.setPageSize(pageSize) ;
      this.renderRows();
    },
    
    onDfltBeanFilter: function(e) {
      this._filter() ;
      this.renderRows() ;
    },
    
    onDfltToolbarAction: function(evt) {
      var actionIdx = parseInt($(evt.target).closest('a').attr('action')) ;
      var actions = this.config.toolbar.dflt.actions ;
      actions[actionIdx].onClick(this) ;
    },
    
    onFilterMoreOption: function(evt) {
      var toolbar = $(evt.target).closest('div.UITableFilterToolbar') ;
      this._toogleBlock(toolbar.find('div.MoreFilterOption')) ;
    },

    onFilter: function(evt) {
      var searchToolbar = $(evt.target).closest('div.UITableFilterToolbar') ;
      var inputs = searchToolbar.find('input') ;
      var query = {fields: {} } ;
      inputs.each(function() {
        var val =  $(this).val() ;
        if(val == null || val == '') return ;
        var name = $(this).attr('name') ;
        var operator = $(this).attr('operator') ;
        query.fields[name] = {operator: operator, value: val} ;
      });
      this.config.toolbar.filter.onFilter(this, query) ;
    },
    
    onSearch: function(evt) {
      var searchToolbar = $(evt.target).closest('div.UITableSearchToolbar') ;
      var input = searchToolbar.find('input') ;
      var query = { query: input.val() } ;
      this.config.toolbar.search.onSearch(this, query) ;
    },
    
    onBeanFieldClick: function(evt) {
      var fieldIdx = parseInt($(evt.target).closest('a').attr('field')) ;
      var row      = parseInt($(evt.target).closest('tr').attr('row')) ;
      var fields = this.config.bean.fields ;
      fields[fieldIdx].onClick(this, row);
    },
    
    onBeanAction: function(evt) {
      var actionIdx = parseInt($(evt.target).closest('a').attr('action')) ;
      var row      = parseInt($(evt.target).closest('tr').attr('row')) ;
      var actions = this.config.bean.actions ;
      actions[actionIdx].onClick(this, row);
    },
    
    onSelectPage: function(evt) {
      var selPage = $(evt.target).closest("a.onSelectPage").attr("page") ;
      var page = parseInt(selPage) ;
      if(page < 1) return ;
      if(page > this.pageList.getAvailablePage()) return ;
      
      this.pageList.getPage(page) ;
      this.renderRows() ;
    },
    
    onAddBean: function() {
      var popupConfig = {title: "New Bean", minWidth: 600, modal: true} ;
      var uicomp = new UITableBean().init(this, {}, -1) ;
      UIPopup.activate(uicomp, popupConfig) ;
    },
    
    onEditBean: function(row) {
      var bean = this.getItemOnCurrentPage(row) ;
      var uicomp = new UITableBean().init(this, bean, row) ;
      var popupConfig = {title: "Edit Bean", minWidth: 600, modal: true} ;
      UIPopup.activate(uicomp, popupConfig) ;
    },
    
    _filter: function() {
      var toolbar = this.$el.find('.UITableDefaultToolbar') ;
      var filterVal = toolbar.find('input.onDfltBeanFilter').val() ;
      if(filterVal != null && filterVal.length > 0) {
        var filterField = toolbar.find('select.onDfltBeanFilter').find(':selected').val() ;
        var holder = [] ;
        for(var i = 0;i < this.state.beans.length; i++) {
          var beanState = this.state.beans[i] ;
          var bean = beanState.bean ;
          if(bean[filterField].indexOf(filterVal) >= 0) {
            holder.push(beanState) ;
          }
        }
        this.pageList = new PageList(this.state.pageSize, holder) ;
      } else {
        this.pageList = new PageList(this.state.pageSize, this.state.beans) ;
      }
    },
    
    _toogleBlock: function(block) {
      if(block.css('display') == 'none') {
        block.css('display', 'block') ;
      } else {
        block.css('display', 'none') ;
      }
    },
    
    _updateBeanStateCount : function() {
      var modifiedCount = 0 ;
      var deletedCount  = 0 ;
      var newCount  = 0 ;
      for(var i = 0; i < this.state.beans.length; i++) {
        var bean = this.state.beans[i].bean ;
        if(bean.persistableState == 'MODIFIED') modifiedCount++ ;
        else if(bean.persistableState == 'DELETED') deletedCount++ ;
        else if(bean.persistableState == 'NEW') newCount++ ;
      }
      this.state.newCount = newCount ;
      this.state.modifiedCount = modifiedCount ;
      this.state.deletedCount = deletedCount ;
    },
    
    randomId: function() {
      var text = "";
      var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      for( var i=0; i < 5; i++ ) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      }
      return text;
    }
  });
  
  return UITable ;
});
