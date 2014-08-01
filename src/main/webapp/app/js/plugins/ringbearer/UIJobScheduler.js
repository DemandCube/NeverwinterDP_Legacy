define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/UITable',
  'ui/UIBean',
  'ui/UIContent',
  'plugins/ringbearer/UIRingBearerJob'
], function($, _, Backbone, ClusterGateway, UICollabsible, UITable, UIBean, UIContent, UIRingBearerJob) {
 
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
                action:'detail', label: "Detail", icon: "gear",
                onClick: function(thisUI, beanConfig, beanState) { 
                  var job = beanState.bean ;
                  var uiRingBearerJob = new UIRingBearerJob({ringbearerJob: job}) ;
                  var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(uiRingBearerJob) ;
                }
              }
            ]
          }
        }
      }
    },

    onInit: function(options) {
      var job = options.ringbearerJob ;
      if(job == null) {
        job = {tasks: []} ;
      }
      this.bind('job', job, false) ;
      this.setReadOnly(true);
    }
  });

  var UIListRingBearerJob = UITable.extend({
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
            field: "description",   label: "Description", toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              var uiRingBearerJob = new UIRingBearerJob({ringbearerJob: bean}) ;
              var uiBreadcumbs = thisTable.getAncestorOfType('UIBreadcumbs') ;
              uiBreadcumbs.push(uiRingBearerJob) ;
            }
          },
          { field: "id",   label: "ID", toggled: true, }
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
      var results = ClusterGateway.execute('ringbearer scheduler --member-role ringbearer') ;
      var fromMember = results[0].fromMember ;
      var schedulerInfo = results[0].result ;

      this.clear() ;
      this.add(new UIRunningJob({ringbearerJob: schedulerInfo.runningJob, fromMember: fromMember})) ;

      var uiWaittingJobs = new UIListRingBearerJob() ;
      uiWaittingJobs.setBeans(schedulerInfo.waittingJobs);
      this.add(uiWaittingJobs) ;

      var uiFinishedJobs = new UIListRingBearerJob() ;
      uiFinishedJobs.label = "Finished Jobs";
      uiFinishedJobs.setBeans(schedulerInfo.finishedJobs);
      this.add(uiFinishedJobs) ;
    }
  }) ;
  
  return UIJobScheduler ;
});
