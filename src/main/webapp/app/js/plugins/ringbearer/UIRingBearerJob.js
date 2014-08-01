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
      this.bind('jobInfo', options.ringbearerJob, false) ;
      this.setReadOnly(true);
    }
  });

  var UIRingBearerJob = UIContainer.extend({
    label: "RingBearer Job", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      this.ringbearerJob = options.ringbearerJob ;
      if(this.ringbearerJob == null) {
        this.ringbearerJob = {
        } ;
      }
      this.fromMember = options.fromMember == null ? {} : options.fromMember ;

      this.setHideHeader(true);
      this.setHideFooter(true);

      this.add(new UIJobInfo({ringbearerJob: this.ringbearerJob})) ;

      var uiScriptProps = new UIContent({content: JSON.stringify(this.ringbearerJob.scriptProperties, null, "  ")}) ;
      uiScriptProps.label = "Script Properties" ;
      this.add(uiScriptProps) ;

      var uiScript = new UIContent({content: this.ringbearerJob.script, highlightSyntax: 'javascript'}) ;
      uiScript.label = "Script" ;
      this.add(uiScript) ;

      var consoleOutput = "No Console Output available" ;
      if(this.ringbearerJob.outputAttributes != null) {
        consoleOutput = this.ringbearerJob.outputAttributes.consoleOutput ;
      }
      this.add(new UIContent({content: consoleOutput})) ;
    }
  }) ;

  return UIRingBearerJob ;
});
