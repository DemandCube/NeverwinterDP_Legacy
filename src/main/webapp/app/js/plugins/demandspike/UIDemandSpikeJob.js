define([
  'jquery',
  'underscore', 
  'backbone',
  'ui/UIContainer',
  'ui/UIBean',
], function($, _, Backbone, UIContainer, UIBean) {

  var UIJobConifg = UIBean.extend({
    label: "Job Config",

    config: {
      beans: {
        jobConfig: {
          name: 'jobConfig', label: 'Job',
          fields: [
            { field: "id",   label: "ID" },
            { field: "numOfTask",   label: "Num Of Task" },
            { field: "numOfThread",   label: "Num Of Thread" },
          ]
        }
      }
    },

    onInit: function(options) {
      this.bind('jobConfig', options.demandspikeJob, false) ;
      this.setReadOnly(true);
    }
  });

  var UITaskConfig = UIBean.extend({
    label: "Task Config",

    config: {
      beans: {
        taskConfig: {
          name: 'taskConfig', label: 'Task Config',
          fields: [
            { field: "type",   label: "Type" },
            { field: "messageSize",   label: "Message Size" },
            { field: "maxDuration",   label: "Max Duration" },
            { field: "maxNumOfMessage",   label: "Max Num Of Message" },
            { field: "sendPeriod",   label: "Send Period" },
          ]
        }
      }
    },

    onInit: function(options) {
      this.bind('taskConfig', options.taskConfig, false) ;
      this.setReadOnly(true);
    }
  });

  var UIDemandSpikeJob = UIContainer.extend({
    label: "Running DemandSpike Job", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      this.demandspikeJob = options.demandspikeJob ;
      if(this.demandspikeJob == null) {
        this.demandspikeJob = {
          taskConfig: {}
        } ;
      }
      this.fromMember = options.fromMember == null ? {} : options.fromMember ;

      this.setHideHeader(true);
      this.setHideFooter(true);

      this.add(new UIJobConifg({demandspikeJob: this.demandspikeJob})) ;
      this.add(new UITaskConfig({taskConfig: this.demandspikeJob.taskConfig})) ;
    }
  }) ;

  return UIDemandSpikeJob ;
});
