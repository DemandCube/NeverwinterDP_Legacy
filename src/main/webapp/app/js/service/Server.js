define([
  'jquery'
], function($) {
  /**@type service.Server */
  Server = {
    /**@memberOf service.Server */
    syncGETResource : function(path, dataType) {
      var returnData = null ;
      $.ajax({ 
        type: "GET",
        dataType: dataType,
        url:  path,
        async: false ,
        error: function(data) {
          console.log(data) ;
          console.trace() ;
        },
        success: function(data) {  returnData = data ; }
      });
      return returnData ;
    },
    
    /**@memberOf service.Server */
    GET : function(request) {
      var returnData = null ;
      $.ajax({ 
        type: "GET",
        dataType: "json",
        url: '../rest/get?req=' + JSON.stringify(request),
        data: params ,
        async: false ,
        error: function(data) {  
          console.debug("Error: \n" + JSON.stringify(data)) ; 
        },
        success: function(data) {  returnData = data ; }
      });
      return returnData ;
    },
    
    /**@memberOf service.Server */
    POST : function(path, request) {
      var returnData = null ;
      $.ajax({ 
        async: false ,
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: path,
        data:  JSON.stringify(request) ,
        error: function(data) {  
          console.debug("Error: \n" + JSON.stringify(data)) ; 
        },
        success: function(data) {  
          returnData = data ; 
        }
      });
      return returnData ;
    },

    /**@memberOf service.Server */
    clusterRequest : function(command) {
      var request = { command: command }
      var response = this.POST("/cluster/rest", request) ;
      if(response.data == null) {
        console.log("Request Error") ;
        console.printJSON(response) ;
      }
      return response.data ;
    },
  };
  return Server ;
});
