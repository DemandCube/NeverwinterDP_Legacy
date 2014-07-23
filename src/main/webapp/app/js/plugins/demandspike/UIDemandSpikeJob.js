define([
  'jquery',
  'underscore', 
  'backbone',
  'ui/UIContainer',
  'ui/UIBean',
  'ui/UITable',
  'ui/UIContent',
], function($, _, Backbone, UIContainer, UIBean, UITable, UIContent) {

  var UIJobInfo = UIBean.extend({
    label: "Job Config",

    config: {
      beans: {
        jobInfo: {
          name: 'jobInfo', label: 'Job',
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
      this.bind('jobInfo', options.demandspikeJob, false) ;
      this.setReadOnly(true);
    }
  });

  var UITasks = UITable.extend({
    label: "Task Command",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'Task Command',
        fields: [
          { field: "description",   label: "Description", toggled: true },
          { field: "command",   label: "Command", toggled: true },
        ]
      }
    },

    onInit: function(options) {
      this.setBeans(options.tasks) ;
    }
  });

  var UIDemandSpikeJob = UIContainer.extend({
    label: "DemandSpike Job", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      this.demandspikeJob = options.demandspikeJob ;
      if(this.demandspikeJob == null) {
        this.demandspikeJob = {
        } ;
      }
      this.fromMember = options.fromMember == null ? {} : options.fromMember ;

      this.setHideHeader(true);
      this.setHideFooter(true);

      this.add(new UIJobInfo({demandspikeJob: this.demandspikeJob})) ;

      this.add(new UITasks({tasks: this.demandspikeJob.tasks})) ;
    }
  }) ;

  return UIDemandSpikeJob ;
});
