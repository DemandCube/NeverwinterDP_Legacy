define([
  'jquery',
  'plugins/cluster/Plugin',
  'plugins/demandspike/Plugin',
  'plugins/elasticsearch/Plugin',
  'plugins/uidemo/Plugin',
], function($, ClusterPlugin, DemandSpikePlugin, ElasticsearchPlugin, UIDemoPlugin) {
  var PluginManager = {
    plugins: [] ,
    
    size: function() { return this.plugins.length ; },

    getPlugin: function(name) {
      for(var i = 0; i < this.plugins.length; i++) {
        if(name == this.plugins[i].name) return this.plugins[i] ;
      }
      return null ;
    },

    getPlugins: function() { return this.plugins ; },

    addPlugin: function(Plugin) {
      this.plugins.push(Plugin) ;
    }
  };

  PluginManager.addPlugin(ClusterPlugin) ;
  PluginManager.addPlugin(DemandSpikePlugin) ;
  PluginManager.addPlugin(ElasticsearchPlugin) ;
  PluginManager.addPlugin(UIDemoPlugin) ;

  return PluginManager ;
});
