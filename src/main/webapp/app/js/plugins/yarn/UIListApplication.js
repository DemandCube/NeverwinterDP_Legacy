define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/Server',
  'ui/UICollapsible',
  'ui/UITable',
  'plugins/yarn/UIApplication',
], function($, _, Backbone, Server, UICollabsible, UITable, UIApplication) {
 
  var UIApplications = UITable.extend({
    label: "List Yarn Applications",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'List Yarn Applications',
        fields: [
          { 
            field: "appId",   label: "App Id", toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              var uiApplication = new UIApplication({appId: bean.appId}) ;
              var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              uiBreadcumbs.push(uiApplication) ;
            }
          },
          { field: "appStartTime",   label: "App Start Time", toggled: true, },
          { field: "appFinishTime",   label: "App Finish Time", toggled: true, },
          { field: "appState",   label: "App State", toggled: true, }
        ]
      }
    }
  });

  var UIListApplication = UICollabsible.extend({
    label: "Job Scheduler Status", 
    config: {
      actions: [
        {
          action: "refresh", label: "Refresh",
          onClick: function(thisUI) {
            thisUI.onRefresh() ;
            thisUI.render() ;
          }
        },
        { 
          action: "back", label: "Back",
          onClick: function(thisUI) {
            console.log("on click back") ;
          }
        }
      ]
    },

    onInit: function(options) {
      this.onRefresh() ;
    },

    onRefresh: function() {
      var appDescriptions = Server.syncGETResource("/yarn-app/history/list", "json") ;

      var uiApplications = new UIApplications() ;
      uiApplications.setBeans(appDescriptions);
      this.add(uiApplications) ;
    }
  }) ;
  
  return UIListApplication ;
});
