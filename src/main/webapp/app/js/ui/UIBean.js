define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UIUtil',
  'text!ui/UIBean.jtpl',
  'css!ui/UIBean.css'
], function($, _, Backbone, UIUtil, UIBeanTmpl) {
  var SearchFilterTemplate = _.template(
    "<h4>Search For <%=fieldConfig.label%>(<%=items.length%>)</h4>" +
    "<%for(var i = 0; i < items.length; i++) { %>" +
    "  <a href='' class='onSelectCustomInput ui-internal-link' itemIdx='<%=i%>'>" +
    "    <%=items[i].label%>" + 
    "  </a>" +
    "<%}%>"
  ) ;
  
  var SearchFilter = function(fieldConfig, bean, evt) {
    var typingTimer = null ;
    var searchInput = $(evt.target).closest('div.UIBeanCustomInput') ;
    var selectorUI = searchInput.find('div.UIBeanCustomInputSelector') ;
    selectorUI.css('width', searchInput.css('width')) ;
    var delaySearch = function(term) {
      var result = fieldConfig.custom.autocomplete.search(term, bean) ;
      fieldConfig.custom.autocomplete.searchResult = result ;
      var params = {
        fieldConfig: fieldConfig,  items: result 
      };
      selectorUI.html(SearchFilterTemplate(params)) ;
      selectorUI.css('display', 'block') ;
    }; 
    
    this.search = function(term) {
      clearTimeout(typingTimer);
      if(term.length == 0) {
        selectorUI.css('display', 'none') ;
        return ;
      }
      typingTimer = setTimeout(function() { delaySearch(term); }, 500 );
    };
  };
  
  /**
   *@type ui.UIBean 
   */
  var UIBean = Backbone.View.extend({
    
    initialize: function (options) {
      //create a clean config for each instance ;
      this.config = $.extend(true, {}, this.config);
      this.beanStates = {} ;
      this.onInit(options) ;
      _.bindAll(this, 'render', 'onToggleBean', 'onAddBean', 'onDeleteBean', 'onRestoreBean',
                'onEditMode', 'onViewMode',
                'onChangeInput', 'onAddInput', 
                'onChangeCustomInput', 'onBlurCustomInput',
                'onFocusDatePickerInput', 'onBlurDatePickerInput',
                'onEditAction', 'onViewAction') ;
    },
    
    onInit: function (options) { },
    
    /**@memberOf ui.UIBean*/
    bind: function(name, bean, setDefault) {
      if(setDefault) {
        var beanConfig = this._getBeanConfig(name) ;
        this._setDefaultFieldValue(beanConfig, bean) ;
      }
      var size = Object.keys(this.beanStates).length ;
      var select = false ;
      if(size == 0) select = true ;
      this.beanStates[name] = { bean: bean, editMode: false, select: select} ;
    },
    
    /**@memberOf ui.UIBean*/
    bindArray: function(name, array) {
      if(this.config.type != 'array') {
        throw new Error("Expect config type is array") ;
      }
      if(array.length == 0) array.push({});
      this.beanArray = array ;
      this.beanName = name ;
      for(var i = 0; i < array.length; i++) {
        this.beanStates[name + '_' + i] = { bean: array[i], editMode: false, select: false} ;
      }
      this._toggleBean(name + '_' + i) ;
    },
    
    getBeanState: function(name) {
      return this.beanStates[name] ;
    },
    
    setReadOnly: function(readOnly) {
      for(var name in this.beanStates) {
        this.beanStates[name].readOnly = readOnly ;
      }
    },
    
    getBean: function(name) {
      return this.beanStates[name].bean ;
    },
    
    restoreBeanState: function(beanName) {
      var beanState = this.beanStates[beanName] ;
      var bean = beanState.bean ;
      if(beanState.origin != null) {
        for(var name in bean) delete bean[name] ;
        $.extend(true, beanState.bean, beanState.origin) ;
      }
      this.render() ;
    },

    getAncestorOfType: function(type) {
      return UIUtil.getAncestorOfType(this, type) ;
    },
    
    _template: _.template(UIBeanTmpl),
    
    render: function() {
      var params = {
        config: this.config, beanStates: this.beanStates 
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create");
    },
    
    events: {
      'click a.onToggleBean': 'onToggleBean',
      'click a.onAddBean': 'onAddBean',
      'click a.onDeleteBean': 'onDeleteBean',
      'click a.onRestoreBean': 'onRestoreBean',
      
      'click a.onEditMode': 'onEditMode',
      'click a.onViewMode': 'onViewMode',
      
      'click a.onFieldClick':  'onFieldClick',
      'click a.onActionClick': 'onActionClick',
      
      'blur   .onChangeInput': 'onChangeInput',
      'click a.onAddInput': 'onAddInput',
      
      'keyup .onChangeCustomInput': 'onChangeCustomInput',
      'blur  .onBlurCustomInput': 'onBlurCustomInput',
      'mousedown  .onSelectCustomInput': 'onSelectCustomInput',
      
      'blur  .onBlurDatePickerInput': 'onBlurDatePickerInput',
      'focus .onFocusDatePickerInput' : "onFocusDatePickerInput",
      
      'click a.onEditAction': 'onEditAction',
      'click a.onViewAction': 'onViewAction'
    },
    
    onToggleBean: function(evt) {
      var beanName = $(evt.target).closest("a").attr('bean') ;
      this._toggleBean(beanName) ;
      this.beanStates[beanName].select = true ;
      this.render() ;
    },
    
    onAddBean: function(evt) {
      var idx = this.beanArray.length ;
      this.beanArray.push({}) ;
      var beanName = this.beanName + '_' + idx ;
      this.beanStates[beanName] = { bean: this.beanArray[idx], editMode: true, select: false} ;
      this._toggleBean(beanName) ;
      this.render() ;
    },

    onRestoreBean: function(evt) {
      var beanName = $(evt.target).closest("div.UIBean").attr('bean') ;
      this.restoreBeanState(beanName) ;
    },
    
    onDeleteBean: function(evt) {
      var beanName = $(evt.target).closest("div.UIBean").attr('bean') ;
      var holder = [] ;
      var idx = 0 ;
      this.beanArray.length = 0 ;
      for(var name in this.beanStates) {
        if(name == beanName) continue ;
        holder[this.beanName + '_' + idx] = this.beanStates[name] ;
        this.beanArray.push(this.beanStates[name].bean) ;
        idx++ ;
      }
      if(idx == 0) {
        var bean = {} ;
        this.beanArray.push(bean) ;
        holder[this.beanName + '_' + idx] = { bean: bean, editMode: true, select: false} ;
      }
      this.beanStates = holder ;
      this._toggleBean(null) ;
      this.render() ;
    },
    
    onEditMode: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      beanState.editMode = true ;
      this.render() ;
    },

    onViewMode: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var bean = beanState.bean ;
      var beanConfig = this._getBeanConfig(beanName) ;
      
      var fields = beanConfig.fields ;
      for(var i = 0; i < fields.length; i++) {
        var fieldName = fields[i].field ;
        var value = bean[fieldName] ;
        if (value instanceof Array) {
          var tmpArray = [] ;
          for(var j = 0; j < value.length; j++) {
            var arrVal = value[j] ;
            if(arrVal.trim != null) arrVal = arrVal.trim() ;
            if(arrVal != '') tmpArray.push(arrVal) ;
          }
          bean[fieldName] = tmpArray ;
        }
      }
      if(this._validateBeanState(beanConfig, beanState)) {
        beanState.editMode = false ;
      }
      this.render() ;
    },
    
    onEditAction: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var beanState = this.beanStates[beanName] ;
      var actionName = $(evt.target).closest("a").attr("action") ;
      var actions = beanConfig.edit.actions ;
      for(var i = 0; i < actions.length; i++) {
        if(actions[i].action == actionName) {
          actions[i].onClick(this, beanConfig, beanState) ;
          return ;
        }
      }
    },
    
    onViewAction: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanConfig = this.config.beans[beanName] ;
      var beanState = this.beanStates[beanName] ;
      var actionName = $(evt.target).closest("a").attr("action") ;
      for(var i = 0; i < beanConfig.view.actions.length; i++) {
        var action = beanConfig.view.actions[i] ;
        if(action.action == actionName) {
          action.onClick(this, beanConfig, beanState) ;
        }
      }
    },
    
    onChangeInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var beanConfig = this._getBeanConfig(beanName) ;
      
      var bean = beanState.bean ;
      var fieldName = $(evt.target).attr('name') ;
      var value     = $(evt.target).val() ;
      var dotIdx = fieldName.lastIndexOf("@");
      var idx = -1 ;
      if(dotIdx > 0) {
        idx = parseInt(fieldName.substring(dotIdx + 1)) ;
        fieldName = fieldName.substring(0, dotIdx) ;
      }
      
      var field = this._getBeanFieldConfig(beanConfig, fieldName) ;
      if(field.multiple) {
        if(bean[fieldName][idx] !=  value && beanState.origin == null) {
          beanState.origin = $.extend(true, {}, bean) ;
        }
        bean[fieldName][idx] =  value ;
      } else {
        if(bean[fieldName] != value && beanState.origin == null) {
          beanState.origin = $.extend(true, {}, bean) ;
        }
        bean[fieldName] = value ;
      }
    },
    
    onAddInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var bean = beanState.bean ;
      
      if(beanState.origin == null) {
        beanState.origin = $.extend(true, {}, bean) ;
      }
      
      var inputGrid = $(evt.target).closest('.InputGrid') ;
      var fieldName = inputGrid.attr('field') ;
      var inputBlock = inputGrid.find('.InputBlock') ;
      console.log("field name = " + fieldName )
      var array = bean[fieldName] ;
      array.push('');
      var idx = array.length - 1 ;
      inputBlock.append(
        '<input class="onChangeInput span-9 text last" type="text" name="' + (fieldName + '@'  + idx)+ '"' + 
        '       value="" data-mini="true" />'
      ) ;
      inputBlock.trigger("create") ;
    },
    
    onChangeCustomInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var fieldName = $(evt.target).attr('name') ;
      var fieldConfig = this._getBeanFieldConfig(beanConfig, fieldName) ;
      
      if(fieldConfig.searchFilter == null) {
        fieldConfig.searchFilter = new SearchFilter(fieldConfig, beanState.bean, evt) ;
      }
      fieldConfig.searchFilter.search($(evt.target).val());
      beanState.onChangeCustomInput = true ;
    },
    
    onBlurCustomInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var fieldName = $(evt.target).attr('name') ;
      var fieldConfig = this._getBeanFieldConfig(beanConfig, fieldName) ;
      var val = $(evt.target).val() ;
      if(val.indexOf("$@") == 0) {
        console.log("animation test: " + val) ;
        val = val.substring(2) ;
        var searchInputBlock = $(evt.target).closest('div.UIBeanCustomInput') ;
        var input = searchInputBlock.find('input'); 
        var bean = beanState.bean ;
        var selectObj = fieldConfig.custom.autocomplete.search(val)[0] ;
        fieldConfig.custom.set(bean, selectObj.value) ;
        input.val(fieldConfig.custom.getDisplay(bean)) ;
      } else if(beanState.onChangeCustomInput && !beanState.onSelectCustomInput) {
        var bean = beanState.bean ;
        var fieldName = $(evt.target).attr('name') ;
        bean[fieldName] = null; //$(evt.target).val() ;
        $(evt.target).val('') ;
      }
      delete beanState.onSelectCustomInput ;
      delete beanState.onChangeCustomInput  ;
      delete fieldConfig.searchFilter ;
      var searchSelector = $(evt.target).closest('div.UIBeanCustomInput').find('div.UIBeanCustomInputSelector') ;
      searchSelector.css('display', 'none') ;
    },
    
    onSelectCustomInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var beanState = this.beanStates[beanName] ;
      var bean = beanState.bean ;
      var searchInputBlock = $(evt.target).closest('div.UIBeanCustomInput') ;
      var input = searchInputBlock.find('input'); 
      var fieldName = input.attr('name') ;
      var fieldConfig = this._getBeanFieldConfig(beanConfig, fieldName);
      var selectIdx = $(evt.target).attr('itemIdx') ;
      var selectObj = fieldConfig.custom.autocomplete.searchResult[selectIdx].value ;
      fieldConfig.custom.set(bean, selectObj) ;
      input.val(fieldConfig.custom.getDisplay(bean)) ;
      searchInputBlock.find('div.UIBeanCustomInputSelector').css('display', 'none') ;
      beanState.onSelectCustomInput = true ;
    },
    
    onFocusDatePickerInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var fieldName = $(evt.target).attr('name') ;
      var fieldConfig = this._getBeanFieldConfig(beanConfig, fieldName) ;
      console.log('on focus date picker ' + fieldConfig.field) ;
    },

    onBlurDatePickerInput: function(evt) {
      var beanName = $(evt.target).closest("[bean]").attr('bean') ;
      var beanState = this.beanStates[beanName] ;
      var bean = beanState.bean ;
      var beanConfig = this._getBeanConfig(beanName) ;
      var fieldName = $(evt.target).attr('name') ;
      var fieldConfig = this._getBeanFieldConfig(beanConfig, fieldName) ;
      setTimeout(function() {
        var dateVal = $(evt.target).val() ;
        fieldConfig.datepicker.setDate(dateVal, bean) ;
      }, 300) ;
    },
    
    validate: function(beanName) {
      error = false ;
      for(var name in this.beanStates) {
        var beanConfig = this._getBeanConfig(name) ;
        var beanState = this.beanStates[name] ;
        if(!this._validateBeanState(beanConfig, beanState)) {
          error = true ;
        }
      }
      return !error ;
    },
    
    _setDefaultFieldValue: function(beanConfig, bean) {
      var fields = beanConfig.fields ;
      for(var i = 0; i < fields.length; i++) {
        var field = fields[i] ;
        var fieldValue = bean[field.field] ;
        if(fieldValue == null && field.defaultValue != null) {
          if(field.defaultValue instanceof Array) {
            fieldValue = field.defaultValue.slice();
          } else {
            fieldValue = field.defaultValue ;
          }
          bean[field.field] = fieldValue ;
        } else if(fieldValue == null && field.select != null) {
          var options = field.select.getOptions() ;
          if(options != null && options.length > 0) {
            bean[field.field] = options[0].value ;
          }
        }
      }
    },
    
    _getBeanConfig: function(beanName) {
      if(this.config.type == 'array') {
        var idx = beanName.lastIndexOf('_');
        var configName = beanName.substring(0, idx);
        return this.config.beans[configName] ;
      } else {
        return this.config.beans[beanName]; 
      }
    },
    
    _getBeanFieldConfig: function(beanConfig, fieldName) {
      for(var i = 0; i < beanConfig.fields.length; i++) {
        var field = beanConfig.fields[i] ;
        if(fieldName == field.field) return field ; 
      }
    },
    
    _toggleBean: function(beanName) {
      var defaultName = null ;
      for(var name in this.beanStates) {
        if(defaultName == null) defaultName = name ;
        this.beanStates[name].select = false ;
      }
      if(this.beanStates[beanName] != undefined) {
        this.beanStates[beanName].select = true ;
      } else {
        this.beanStates[defaultName].select = true ;
      }
    },
    
    _validateBeanState: function(beanConfig, beanState) {
      var fields = beanConfig.fields ;
      beanState.fieldErrors = {} ;
      var hasError = false ;
      for(var i = 0; i < fields.length; i++) {
        var field = fields[i].field ;
        var value = beanState.bean[field];
        if(fields[i].required && _.isEmpty(value)) {
          beanState.fieldErrors[field] = "This field cannot be empty" ;
          hasError = true ;
          continue ;
        }
        var validator = fields[i].validator ;
        if(validator == null) continue ;
        if(UIUtil.Validator[validator.name] != undefined) {
          var validate = UIUtil.Validator[validator.name] ;
          try {
            if (value instanceof Array) {
              for(var j = 0; j < value.length; j++) {
                beanState.bean[field][j] = validate(validator, value[j]) ;
              }
            } else {
              beanState.bean[field] = validate(validator, value) ;
            }
          } catch(err) {
            beanState.fieldErrors[field] = err ;
            hasError = true ;
          }
        } else {
          throw new Error("Unknown validator " + validator) ;
        }
      }
      return !hasError ;
    }
  });
  
  return UIBean ;
});
