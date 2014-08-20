define([
  'jquery',
  'underscore', 
  'backbone',
  'service/Server',
  'ui/UIContainer',
  'ui/UIBean',
  'ui/UITable',
], function($, _, Backbone, Server, UIContainer, UIBean, UITable) {

  var UIAppInfo = UIBean.extend({
    label: "App Info",

    config: {
      beans: {
        appInfo: {
          name: 'appInfo', label: 'App Info',
          fields: [
            { field: "appId",   label: "App Id" },
            { field: "appHome",   label: "App Home" },
          ]
        }
      }
    },

    onInit: function(options) {
      this.bind('appInfo', options.appHistory.appInfo, false) ;
      this.setReadOnly(true);
    }
  });

  var UIContainers = UITable.extend({
    label: "Containers",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'Containers',
        fields: [
          { 
            field: "containerId",   label: "Container Id", toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              //var uiApplication = new UIApplication({appId: bean.appId}) ;
              //var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              //uiBreadcumbs.push(uiApplication) ;
            }
          },
          { field: "nodeId",   label: "Node Id", toggled: true, },
          { field: "memory",   label: "Memory", toggled: true, },
          { field: "cores",   label: "CPU Cores", toggled: true, },
          { 
            field: "containerState",   label: "Container State", toggled: true, 
            custom: {
              getDisplay: function(bean) { return  bean.progressStatus.containerState ; },
            }
          },
          { 
            field: "containerProgress",   label: "Container Progress", toggled: true, 
            custom: {
              getDisplay: function(bean) { return  bean.progressStatus.progress ; },
            }
          },
          { 
            field: "containerStatusMessage",   label: "Status Message", toggled: true, 
            custom: {
              getDisplay: function(bean) { return  bean.progressStatus.statusMessage ; },
            }
          },
          { 
            field: "containerError",   label: "Error", toggled: true, 
            custom: {
              getDisplay: function(bean) { return  bean.progressStatus.error != null ; },
            }
          }
        ]
      }
    }
  });

  var UIApplication = UIContainer.extend({
    label: "Yarn Application", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      var appHistory = Server.syncGETResource("/yarn-app/history/app/" + options.appId, "json") ;

      this.setHideHeader(true);
      this.setHideFooter(true);

      this.add(new UIAppInfo({appHistory: appHistory})) ;

      var uiContainers = new UIContainers() ;
      uiContainers.setBeans(appHistory.appMonitor.containerInfos);
      this.add(uiContainers) ;
    }
  }) ;

  return UIApplication ;
});
