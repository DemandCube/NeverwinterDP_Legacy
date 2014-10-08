define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UITable',
  'plugins/elasticsearch/ESGateway',
  'plugins/elasticsearch/UINodeDetail'
], function($, _, Backbone, UITable, ESGateway, UINodeDetail) {
  
  var UINodes = UITable.extend({
    label: "Elasticsearch Nodes",
    config: {
      toolbar: {
        dflt: {
          actions: [
            {
              action: "onRefresh", icon: "refresh", label: "Refresh", 
              onClick: function(thisTable) { 
                console.log("on refresh");
              } 
            }
          ]
        }
      },
      
      bean: {
        label: 'Elasticsearch Nodes',
        fields: [
          { 
            field: "_id",  label: "Id",toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              thisTable.UIParent.push(new UINodeDetail( {nodeInfo: bean})) ;
            },
          },
          { field: "name",  label: "Name",toggled: true, filterable: true },
          { field: "host",  label: "Host",toggled: true, filterable: true },
          { field: "transport_address",  label: "Transport Addr", toggled: true },
          { field: "http_address",  label: "Http Addr", toggled: true }
        ]
      }
    },

    onInit: function(options) {
      this.onRefresh() ;
    },

    onRefresh: function() {
      var result = ESGateway.nodes.nodes() ;
      var nodes = [] ;
      for(var key in result.nodes) {
        var value = result.nodes[key] ;
        value._id = key ;
        nodes.push(value) ;
      }
      this.setBeans(nodes) ;
    }
  });
  
  return UINodes ;
});
