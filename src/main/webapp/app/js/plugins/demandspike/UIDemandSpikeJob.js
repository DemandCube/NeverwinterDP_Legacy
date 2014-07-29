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
          ]
        }
      }
    },

    onInit: function(options) {
      this.bind('jobInfo', options.demandspikeJob, false) ;
      this.setReadOnly(true);
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

      var uiScriptProps = new UIContent({content: JSON.stringify(this.demandspikeJob.scriptProperties, null, "  ")}) ;
      uiScriptProps.label = "Script Properties" ;
      this.add(uiScriptProps) ;

      var uiScript = new UIContent({content: this.demandspikeJob.script, highlightSyntax: 'javascript'}) ;
      uiScript.label = "Script" ;
      this.add(uiScript) ;

      var consoleOutput = "No Console Output available" ;
      if(this.demandspikeJob.outputAttributes != null) {
        consoleOutput = this.demandspikeJob.outputAttributes.consoleOutput ;
      }
      this.add(new UIContent({content: consoleOutput})) ;
    }
  }) ;

  return UIDemandSpikeJob ;
});
