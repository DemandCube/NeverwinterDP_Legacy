
define([
  'service/Server'
], function(Server) {
  var gatewayAddr = "http://127.0.0.1:9200"; 

  function GET(path) { 
    return Server.syncGETResource(gatewayAddr + path, "json") ; 
  }

  var ESGateway = {
    cluster: {
      health: function() { return GET("/_cluster/health") ; },

      stats: function() { return GET("/_cluster/stats") ; }
    },

    nodes: {
      nodes: function() { return GET("/_nodes") ; }
    },

    index: {
      indices: function() { return GET("/_stats/indices,store,docs") ; }
    },

    stats: {
      all: function() { return GET("/_stats") ; }
    },

  }
  return ESGateway ;
});
