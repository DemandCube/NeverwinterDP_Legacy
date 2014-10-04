define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UITableTree'
], function($, _, Backbone, ClusterGateway, UITableTree) {
  var UIClusterMetric = UITableTree.extend({
    label: "Cluster Metrics",

    config: {
      bean: {
        label: 'Cluster Metrics',
        fields: [
          { field: "name", label: "Name", toggled: true},
          { field: "count", label: "Count", toggled: true},
          { field: "min", label: "Min", toggled: true},
          { field: "max", label: "Max", toggled: true},
          { 
            field: "mean", label: "Mean", toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.mean.toFixed(2) ; }
            }
          },
          { 
            field: "stddev", label: "Std Dev", toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.stddev.toFixed(2) ; }
            }
          },
          { field: "p50", label: "P50", toggled: true},
          { field: "p75", label: "P75", toggled: true},
          { field: "p90", label: "P90", toggled: true},
          { field: "p95", label: "P95", toggled: true},
          { field: "p98", label: "P98", toggled: true},
          { field: "p99", label: "P99", toggled: true},
          { field: "p999", label: "P99.9", toggled: true},
          { 
            field: "m1Rate", label: "1 Min", toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m1Rate.toFixed(2) ; }
            }
          },
          { 
            field: "m5Rate", label: "5 Min", toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m5Rate.toFixed(2) ; }
            }
          },
          { 
            field: "m15Rate", label: "15 Min", toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m15Rate.toFixed(2) ; }
            }
          },
          { field: "durationUnits", label: "T Unit", toggled: true},
          { field: "rateUnits", label: "R Unit", toggled: true}
        ]
      }
    },

    onInit: function(options) {
      var snapshot = null;
      console.log("load method: " + options.loadMethod) ;
      if(options.loadMethod == 'yara') {
        snapshot = this.loadYaraClusterMetricSnapshot() ;
      } else {
        snapshot = this.loadCombineClusterMetricSnapshot() ;
      }
      var counters = snapshot.counters ;
      var clusterTimers = snapshot.timers ;
      for(var name in clusterTimers) {
        var clusterTimer = clusterTimers[name];
        clusterTimer.name = name ;
        var combinedTimer = clusterTimer.timer  ;
        combinedTimer.name = name ;
        var node = this.addNode(combinedTimer);
        var timers = clusterTimer.timers ;
        for(var serverName in timers) {
          var serverTimer = timers[serverName] ;
          serverTimer.name = serverName;
          node.addChild(serverTimer) ;
        }
      }
    },

    loadCombineClusterMetricSnapshot: function() {
      var result = ClusterGateway.execute('server metric-cluster-snapshot') ;
      return result.result ;
    },

    loadYaraClusterMetricSnapshot: function() {
      var result = ClusterGateway.execute('yara snapshot --member-name generic') ;
      return result[0].result ;
    }
  });
  
  return UIClusterMetric ;
});
