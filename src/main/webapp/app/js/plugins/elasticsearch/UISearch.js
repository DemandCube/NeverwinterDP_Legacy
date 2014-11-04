define([
  'jquery',
  'underscore', 
  'backbone',
  'ui/UITable',
  'plugins/elasticsearch/ESGateway',
  'text!plugins/elasticsearch/UISearch.jtpl'
], function($, _, Backbone,UITable, ESGateway, Template) {
  var UISearch = Backbone.View.extend({
    initialize: function (options) {
      var index = options.index ;
      this.label = 'Search ' + index ;
      this.queryInput = '*';
      this.indexQuery = ESGateway.query.indexQuery(index) ;
      this.uiSearchResult = this.buildUISearchResult() ;
      this._search(this.queryInput, false) ;
      _.bindAll(this, 'render', 'onSearch') ;
    },
    
    _template: _.template(Template),
    
    render: function() {
      var params = { 
        queryInput: this.queryInput,
        result: this.result
      } ;
      $(this.el).html(this._template(params));
      $(this.el).trigger("create") ;

      this.$('.UISearchResult').unbind() ;
      this.uiSearchResult.setElement(this.$('.UISearchResult')).render();
    },

    events: {
      'click .onSearch': 'onSearch',
      'keyup .onSearchInput': 'onSearchInput',
      'click .onMore': 'onMore',
    },

    onSearch: function(evt) {
      this.queryInput = $(evt.target).parents('.SearchInput').find('input').val() ;
      this._search(this.queryInput, true) ;
    },

    onSearchInput: function(evt) {
      if(evt.keyCode == 13) {
        this.queryInput = $(evt.target).val();
        this._search(this.queryInput, true) ;
      }
    },

    onMore: function(evt) {
      var moreOptionsBlock = $(evt.target).parents('.SearchInput').find('.MoreOptions');
      moreOptionsBlock.toggle() ;
    },

    _search: function(query, render) {
      this.result = this.indexQuery.search(query) ;
      this.uiSearchResult.setBeans(this.result.hits.hits) ;
      if(render) this.render() ;
    },

    buildUISearchResult: function() {
      var mapping = this.indexQuery.mapping ;
      var fields = [] ;
      //since the field name is dynamic according to index name and type, use this trick to get the field names
      if(Object.keys(mapping).length > 0) {
        var mappings = mapping[Object.keys(mapping)[0]] ;
        var record = mappings[Object.keys(mappings)[0]] ;
        var properties = record[Object.keys(record)[0]] ;
        fields = properties[Object.keys(properties)[0]] ;
      }
      var config = {
        toolbar: {
          dflt: {
            actions: [ ]
          }
        },
        
        bean: {
          label: 'Search Result',
          fields: [ ]
        }
      };

      //console.printJSON(fields) ;
      var configFields = config.bean.fields ;
      for(var fieldName in fields) {
        var configField = { 
          field: fieldName,  label: fieldName, toggled: true, filterable: true,
          custom: {
            fieldName: fieldName,
            getDisplay: function(bean) { 
              return  bean._source[this.fieldName] ; 
            }
          }
        };
        configFields.push(configField) ;
      }
      configFields.push({ label: 'Score', field: '_score', toggled: false }) ;
      configFields.push({ label: 'Index', field: '_index', toggled: false }) ;
      configFields.push({ label: 'Type', field: '_type', toggled: false }) ;

      var UISearchResult = UITable.extend({
        label: "Search Result",
        config: config
      }) ;
      var uiSearchResult = new UISearchResult() ;
      uiSearchResult.setBeans([]) ;
      return uiSearchResult ;
    }
  });

  return UISearch ;
});
