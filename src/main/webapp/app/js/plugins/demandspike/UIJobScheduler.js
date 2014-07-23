define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/UITable',
  'ui/UIBean',
  'ui/UIContent',
  'plugins/demandspike/UIDemandSpikeJob'
], function($, _, Backbone, ClusterGateway, UICollabsible, UITable, UIBean, UIContent, UIDemandSpikeJob) {
 
  var UIRunningJob = UIBean.extend({
    label: "Running Job",

    config: {
      beans: {
        job: {
          name: 'job', label: 'Job',
          fields: [
            { field: "id",   label: "ID" },
            { field: "description",   label: "Description" },
          ],
          view: {
            actions: [
              {
                action:'viewConsoleOutput', label: "Console Output", icon: "gear",
                onClick: function(thisUI, beanConfig, beanState) { 
                  var job = beanState.bean ;
                  var consoleOutput = "No Console Output available" ;
                  if(job.outputAttributes != null) {
                    consoleOutput = job.outputAttributes.consoleOutput ;
                  }
                  var uiConsoleOutput = new UIContent({content: consoleOutput}) ;
                  uiConsoleOutput.label = "Console Output" ;
                  var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(uiConsoleOutput) ;
                }
              }
            ]
          }
        }
      }
    },

    onInit: function(options) {
      var job = options.demandspikeJob ;
      if(job == null) job = {} ;
      this.bind('job', job, false) ;
      this.setReadOnly(true);
    }
  });

  var UIListDemandSpikeJob = UITable.extend({
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
              var uiDemandSpikeJob = new UIDemandSpikeJob({demandspikeJob: bean}) ;
              var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              uiBreadcumbs.push(uiDemandSpikeJob) ;
            }
          },
          { field: "description",   label: "Description", toggled: true }
        ]
      }
    }
  });

  var UIJobScheduler = UICollabsible.extend({
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
      var results = ClusterGateway.execute('demandspike scheduler --member-role demandspike') ;
      var fromMember = results[0].fromMember ;
      var schedulerInfo = results[0].result ;

      this.add(new UIRunningJob({demandspikeJob: schedulerInfo.runningJob, fromMember: fromMember})) ;

      var uiWaittingJobs = new UIListDemandSpikeJob() ;
      uiWaittingJobs.setBeans(schedulerInfo.waittingJobs);
      this.add(uiWaittingJobs) ;

      var uiFinishedJobs = new UIListDemandSpikeJob() ;
      uiFinishedJobs.label = "Finished Jobs";
      uiFinishedJobs.setBeans(schedulerInfo.finishedJobs);
      this.add(uiFinishedJobs) ;
    }
  }) ;
  
  return UIJobScheduler ;
});
