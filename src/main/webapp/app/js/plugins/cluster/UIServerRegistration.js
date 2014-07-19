define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UIContainer',
  'ui/UIBean',
  'ui/UITable',
  'plugins/cluster/UIJVMInfo',
  'plugins/cluster/UIMetric',
], function($, _, Backbone, ClusterGateway, UIContainer, UIBean, UITable, UIJVMInfo, UIMetric) {
  var UIMemberInfo = UIBean.extend({
    label: "Member Info",

    config: {
      beans: {
        memberBean: {
          name: 'memberBean', label: 'Member',
          fields: [
            { 
              field: "serverName",   label: "Server Name", defaultValue: '', 
              toggled: true, filterable: true,
              onClick: function(thisTable, row) {
                var bean = thisTable.getItemOnCurrentPage(row) ;
                console.log('on click bean ' + JSON.stringify(bean)) ;
              },
              custom: {
                getDisplay: function(bean) { return  bean.clusterMember.memberName ; }
              }
            },
            { 
              field: "ipAddress",  label: "IP Address",toggled: true,
              custom: {
                getDisplay: function(bean) { return  bean.clusterMember.ipAddress ; }
              }
            },
            { 
              field: "port",  label: "Port",toggled: true,
              custom: {
                getDisplay: function(bean) { return  bean.clusterMember.port ; }
              }
            },
            { 
              field: "serverState",   label: "Server State", defaultValue: '', 
              toggled: true, filterable: true,
              custom: {
                getDisplay: function(bean) { return bean.serverState ; }
              }
            },

            { 
              field: "serverRoles",   label: "Server Roles", defaultValue: '', 
              toggled: true, filterable: true,
              custom: {
                getDisplay: function(bean) { return bean.roles ; }
              }
            }
          ],
          view: {
            actions: [
              {
                action:'jvm', label: "JVM", icon: "gear",
                onClick: function(thisUI, beanConfig, beanState) { 
                  var memberName = beanState.bean.clusterMember.memberName ;
                  var uiJVMInfo = new UIJVMInfo({memberName: memberName}) ;
                  var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(uiJVMInfo) ;
                }
              },
              {
                action:'metric', label: "Metric", icon: "gear",
                onClick: function(thisUI, beanConfig, beanState) { 
                  var memberName = beanState.bean.clusterMember.memberName ;
                  var uiMetric = new UIMetric({memberName: memberName}) ;
                  var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(uiMetric) ;
                }
              }
            ]
          }
        }
      }
    },

    onInit: function(options) {
      this.bind('memberBean', options.registration, false) ;
      this.setReadOnly(true);
    }
  });


  var UIServiceInfo = UITable.extend({
    label: "Service Info",
    config: {
      toolbar: {
        dflt: {
          actions: [
            {
              action: "onRefresh", icon: "refresh", label: "Refresh", 
              onClick: function(thisTable) { 
              } 
            }
          ]
        }
      },
      
      bean: {
        label: 'Service',
        fields: [
          { field: "module",   label: "Module", toggled: true, filterable: true },
          { field: "serviceId",   label: "Service Id", toggled: true, filterable: true },
          { field: "description",   label: "Description", toggled: true, filterable: true },
          { field: "state",   label: "State", toggled: true, filterable: true },
        ]
      }
    }
  });

  var UIServerRegistration = UIContainer.extend({
    label: "Server Registration", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      var registration = null ;
      if(options.serverRegistration != null) {
        registration = options.serverRegistration ;
      } else {
        console.log('member name = ' + options.memberName) ;
        var results = ClusterGateway.execute('server registration --member-name ' + options.memberName) ;
        registration = results[0].result ;
      }
      this.label = "Server Registration For " + registration.clusterMember.memberName ;  

      this.add(new UIMemberInfo({registration: registration})) ;

      var uiServiceInfo = new UIServiceInfo() ;
      uiServiceInfo.setBeans(registration.services) ;
      this.add(uiServiceInfo) ;
    }
  }) ;

  return UIServerRegistration ;
});
