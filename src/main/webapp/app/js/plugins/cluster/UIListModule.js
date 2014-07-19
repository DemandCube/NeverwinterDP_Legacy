define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'ui/UITable',
], function($, _, Backbone, ClusterGateway, UICollapsible, UITable) {

  var UIModule = UITable.extend({
    label: 'Module Info',
    config: {
      toolbar: {
        dflt: {
          actions: []
        }
      },
      bean: {
        fields: [
          { label : 'Module Name', field : 'moduleName', toggled : true, filterable : true},
          { label : 'Configure Class', field : 'configureClass', toggled : true, filterable : true},
          { label : 'Auto Start', field : 'autostart', toggled : true},
          { label : 'Auto Install', field : 'autoInstall', toggled : true},
          { label : 'Install Status', field : 'installStatus', toggled : true},
          { label : 'Running Status', field : 'runningStatus', toggled : true}
        ],
        actions: []
      }
    },
    
    onInit: function (options) {
      this.memberName = options.fromMember.memberName;
      this.label = "Module on " + this.memberName ;

      this.setBeans(options.modules) ;
      return this ;
    }
  });

  var UIListModule = UICollapsible.extend({
    label: "Module Status", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      var results = ClusterGateway.execute('module list') ;
      for(var i = 0; i< results.length; i++) {
        var fromMember = results[i].fromMember ;
        var modules = results[i].result ;
        this.add(new UIModule({fromMember: fromMember, modules: modules})) ;
      }
    }
  }) ;

  return UIListModule ;
});
