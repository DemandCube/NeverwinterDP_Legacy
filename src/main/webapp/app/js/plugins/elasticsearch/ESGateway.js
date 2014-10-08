
define([
  'service/Server'
], function(Server) {
  var gatewayAddr = "http://127.0.0.1:9200"; 

  function GET(path) { 
    return Server.syncGETResource(gatewayAddr + path, "json") ; 
  }

  function POST(path, dataObj) { 
    return Server.syncPOSTJson(gatewayAddr + path, dataObj) ; 
  }

  function Query(index) {
    this.index = index ;
    this.mapping = GET("/" + index + "/_mapping") ;

    this.searchOK = function(query) {
      var query = {
        "query": {
          "query_string" : {
            "default_field" : "_all",
            "query" : 'ERROR'
          }
        }
      };
    }

    this.search = function(query) {
      var query = {
        "from" : 0, "size" : 250,
        "query" : {
          "query_string" : {
            "default_field" : "_all",
            "query" : query
          }
        }
      };
      return POST("/" + index + "/_search", query) ;
    }
  };


  var ESGateway = {
    cluster: {
      health: function() { return GET("/_cluster/health") ; },

      stats: function() { return GET("/_cluster/stats") ; }
    },

    nodes: {
      nodes: function() { return GET("/_nodes") ; }
    },

    index: {
      available: function() { 
        var result =  GET("/_stats/indices") ; 
        var indices = [] ;
        for(var key in result.indices) {
          indices.push(key) ;
        }
        return indices ;
      },

      indices: function() { return GET("/_stats/indices,store,docs") ; }
    },

    query: {
      indexQuery: function(index) { return new Query(index) ; }
    },

    stats: {
      all: function() { return GET("/_stats") ; }
    },

  }
  return ESGateway ;
});
