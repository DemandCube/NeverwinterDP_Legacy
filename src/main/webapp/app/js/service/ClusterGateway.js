define([
  'jquery',
  'service/Server'
], function($, Server) {

  function ClusterRegistration() {
    this.clusterRegistration = Server.clusterRequest("cluster", "registration", {} ) ;

    this.getMembers = function() {
      var serverReg = this.clusterRegistration.serverRegistration ;
      var holder = [] ;
      for(var i = 0; i < serverReg.length; i++) {
        holder.push(serverReg[i].clusterMember) ;
      }
      return holder ;
    }

    this.getMemberByRole = function(role) {
      var serverReg = this.clusterRegistration.serverRegistration ;
      var holder = [] ;
      for(var i = 0; i < serverReg.length; i++) {
        var roles = serverReg[i].roles ;
        if($.inArray(role, roles) >= 0) {
          holder.push(serverReg[i].clusterMember) ;
        }
      }
      return holder ;
    }

    this.getRoleSet = function() {
      var serverReg = this.clusterRegistration.serverRegistration ;
      var roleSet = {} ;
      for(var i = 0; i < serverReg.length; i++) {
        var roles = serverReg[i].roles ;
        for(var j = 0; j < roles.length; j++) {
          roleSet[roles[j]] =  true ;
        }
      }
      return Object.keys(roleSet) ;
    }
  };

  var ClusterGateway = {
    clusterRegistration: new ClusterRegistration()  ,

    getClusterRegistration: function() { return this.clusterRegistration ; } ,

    call: function(group, command, params) {
      return Server.clusterRequest(group, command, params ) ;
    }
  }
  return ClusterGateway ;
});
