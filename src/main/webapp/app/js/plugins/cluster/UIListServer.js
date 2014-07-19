define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UITable',
  'plugins/cluster/UIServerRegistration',
  'plugins/cluster/UIJVMInfo',
  'plugins/cluster/UIMetric',
], function($, _, Backbone, ClusterGateway, UITable, UIServerRegistration, UIJVMInfo, UIMetric) {
  
  var UIListServer = UITable.extend({
    label: "List Server",
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
        label: 'Cluster Member',
        fields: [
          { 
            field: "serverName",   label: "Server Name", defaultValue: '', 
            toggled: true, filterable: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              var memberName = bean.fromMember.memberName ;
              thisTable.UIParent.push(new UIServerRegistration( {memberName: memberName})) ;
            },
            custom: {
              getDisplay: function(bean) {
                return bean.fromMember == null ? null : bean.fromMember.memberName ;
              }
            }
          },
          { 
            field: "ipAddress",  label: "IP Address",toggled: true,
            custom: {
              getDisplay: function(bean) {
                return bean.fromMember == null ? null : bean.fromMember.ipAddress ;
              }
            }
          },
          { 
            field: "port",  label: "Port",toggled: true,
            custom: {
              getDisplay: function(bean) {
                return bean.fromMember == null ? null : bean.fromMember.port ;
              }
            }
          },
          { 
            field: "result",   label: "Status", defaultValue: '', 
            toggled: true, filterable: true
          }
        ],

        actions:[
          {
            icon: "gear", label: "JVM",
            onClick: function(thisTable, row) { 
              var bean = thisTable.getItemOnCurrentPage(row) ;
              var memberName = bean.fromMember.memberName ;
              var uiJVMInfo = new UIJVMInfo( {memberName: memberName} ) ;
              var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              uiBreadcumbs.push(uiJVMInfo) ;
            }
          },
          {
            icon: "gear", label: "Metric",
            onClick: function(thisTable, row) { 
              var bean = thisTable.getItemOnCurrentPage(row) ;
              var memberName = bean.fromMember.memberName ;
              var uiMetric = new UIMetric({memberName: memberName}) ;
              var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              uiBreadcumbs.push(uiMetric) ;
            }
          }
        ]
      }
    },

    onInit: function(config) {
      var result = ClusterGateway.execute('server ping') ;
      this.setBeans(result) ;
    }
  });
  
  return UIListServer ;
});
