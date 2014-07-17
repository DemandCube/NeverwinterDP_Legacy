define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/UITable',
  'plugins/demandspike/UIDemandSpikeJob'
], function($, _, Backbone, ClusterGateway, UICollabsible, UITable, UIDemandSpikeJob) {
  
  var UIWaittingJob = UITable.extend({
    label: "Waitting Jobs",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'Waitting Job',
        fields: [
          { 
            field: "id",   label: "ID", toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              console.log('on click bean ' + JSON.stringify(bean)) ;
            }
          },
          { 
            field: "numOfTask",   label: "Num Of Task", toggled: true
          }
        ],
        actions:[
          {
            icon: "delete", label: "Delete",
            onClick: function(thisTable, row) { 
              thisTable.markDeletedItemOnCurrentPage(row) ;
              console.log('Mark delete row ' + row);
            }
          }
        ]
      }
    }
  });

  var UIStatus = UICollabsible.extend({
    label: "Job Scheduler Status", 
    config: {
      actions: [
        {
          action: "refresh", label: "Refresh",
          onClick: function(thisUI) {
            console.log("on click refresh") ;
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
      var results = ClusterGateway.call('demandspike', 'status', {"member-role" : "demandspike"}) ;
      var fromMember = results[0].fromMember ;
      var job = results[0].result.runningJob ;

      this.add(new UIDemandSpikeJob({demandspikeJob: job, fromMember: fromMember})) ;

      var uiWaittingJob = new UIWaittingJob() ;
      uiWaittingJob.setBeans([]);
      this.add(uiWaittingJob) ;
    }
  }) ;
  
  return UIStatus ;
});
