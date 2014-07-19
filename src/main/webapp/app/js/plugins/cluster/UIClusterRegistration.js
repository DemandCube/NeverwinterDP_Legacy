define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UICollapsible',
  'plugins/cluster/UIServerRegistration',
], function($, _, Backbone, ClusterGateway, UICollapsible, UIServerRegistration) {
  var UIClusterRegistration = UICollapsible.extend({
    label: "Cluster Registration", 
    config: {
      actions: [ ]
    },

    onInit: function(options) {
      var results = ClusterGateway.execute('server registration') ;
      for(var i = 0; i< results.length; i++) {
        var serverRegistration = results[i].result ;
        var uiServerReg = new UIServerRegistration( {serverRegistration: serverRegistration}) ;
        uiServerReg.setHideHeader(true) ;
        uiServerReg.setHideFooter(true) ;
        this.add(uiServerReg, true) ;
      }
    }
  }) ;

  return UIClusterRegistration ;
});
