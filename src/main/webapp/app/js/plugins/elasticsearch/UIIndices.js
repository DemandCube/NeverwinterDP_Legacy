define([
  'ui/UITable',
  'ui/UIBean',
  'ui/UICollapsible',
  'plugins/elasticsearch/ESGateway',
], function(UITable, UIBean, UICollapsible, ESGateway) {
  
  var UIShards = UIBean.extend({
    label: 'Shards',
    config: {
      beans : {
        shards : {
          label: 'Shards',
          fields: [
            { label : 'Total', field: 'total' },
            { label : 'Sucessful', field: 'successful' },
            { label : 'Failed', field: 'failed' },
          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('shards', options._shards) ;
      var beanState = this.getBeanState('shards');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    },
  });

  var UIDocInfo = UIBean.extend({
    label: 'Primaries',
    config: {
      beans : {
        docInfo : {
          label: 'Store',
          fields: [
            { 
              label : 'Docs Count', field: 'docs.count',
              custom: {
                getDisplay: function(bean) { return  bean.docs.count ; }
              }
            },
            { 
              label : 'Docs Deleted', field: 'docs.deleted',
              custom: {
                getDisplay: function(bean) { return  bean.docs.deleted ; }
              }
            },
            { 
              label : 'Store', field: 'store.size_in_bytes',
              custom: {
                getDisplay: function(bean) { return  bean.store.size_in_bytes ; }
              }
            },
            { 
              label : 'Store Throttle Time In Millis', field: 'store.throttle_time_in_millis',
              custom: {
                getDisplay: function(bean) { return  bean.store.throttle_time_in_millis ; }
              }
            },
          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('docInfo', options.docInfo) ;
      var beanState = this.getBeanState('docInfo');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    },
  });

  var UIIndexTable = UITable.extend({
    label: "Indices",
    config: {
      toolbar: {
        dflt: {
          actions: [
            {
              action: "onRefresh", icon: "refresh", label: "Refresh", 
              onClick: function(thisTable) { 
                console.log("on refresh");
              } 
            }
          ]
        }
      },
      
      bean: {
        label: 'Elasticsearch Indices',
        fields: [
          { 
            field: "_name",  label: "Name",toggled: true, filterable: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              //thisTable.UIParent.push(new UINodeDetail( {nodeInfo: bean})) ;
            },
          },
          { 
            field: "promaries.docs",  label: "Documents",toggled: true,
            custom: {
              getDisplay: function(bean) { return  bean.primaries.docs.count ; }
            }
          },
          { 
            field: "primaries.store",  label: "Store",toggled: true,
            custom: {
              getDisplay: function(bean) { return  bean.primaries.store.size_in_bytes ; }
            }
          },
        ]
      }
    },

    onInit: function(options) {
      var indices = [] ;
      for(var key in options.indices) {
        var value = options.indices[key] ;
        value._name = key ;
        indices.push(value) ;
      }
      this.setBeans(indices) ;
    }
  });

  var UIIndices = UICollapsible.extend({
    label: "Indices", 
    config: {
      actions: [
        {
          action: 'back', label: 'Back',
          onClick: function (thisUI) {
            var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
            uiBreadcumbs.back() ;
          }
        },
      ]
    },
    
    onInit: function(options) {
      var result = ESGateway.index.indices() ;
      console.printJSON(result) ;
      this.add(new UIShards(result)) ;

      var uiPrimaries = new UIDocInfo({docInfo: result._all.primaries}) ;
      uiPrimaries.label = "Primaries" ;
      this.add(uiPrimaries) ;

      var uiTotal = new UIDocInfo({docInfo: result._all.total}) ;
      uiTotal.label = "Total" ;
      this.add(uiTotal) ;

      this.add(new UIIndexTable({indices: result.indices})) ;
    }
  });
  
  return UIIndices;
});
