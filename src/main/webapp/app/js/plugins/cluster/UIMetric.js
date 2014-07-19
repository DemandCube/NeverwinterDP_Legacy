
define([
  'jquery', 
  'underscore', 
  'backbone',
  'service/ClusterGateway',
  'ui/UITable',
  'ui/UICollapsible'
], function($, _, Backbone, ClusterGateway, UITable, UICollapsible) {
  var UICounterMetric = UITable.extend({
    label: "Counter Metric",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'Counter Metric',
        fields: [
          { field: "name",   label: "Name", toggled: true, filterable: true },
          { field: "count",   label: "Count", toggled: true, filterable: true }
        ]
      }
    },

    onInit: function(options) {
      this.setBeans(options.counters) ;
    }
  });
  
  var UITimerMetric = UITable.extend({
    label: "Timer Metric",
    config: {
      toolbar: {
        dflt: {
          actions: [ ]
        }
      },
      
      bean: {
        label: 'Timer Metric',
        fields: [
          { field: "name",   label: "Name", toggled: true, filterable: true },
          { field: "count",   label: "Count", toggled: true, filterable: true },
          { field: "max",   label: "Max", toggled: true},
          { 
            field: "mean",   label: "Mean" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.mean.toFixed(2) ; }
            }
          },
          { field: "min",   label: "Min" , toggled: true},
          { 
            field: "p50",   label: "p50" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p50.toFixed(2) ; }
            }
          },
          { 
            field: "p75",   label: "p75" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p75.toFixed(2) ; }
            }
          },
          { 
            field: "p95",   label: "p95" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p95.toFixed(2) ; }
            }
          },
          { 
            field: "p98",   label: "p98" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p98.toFixed(2) ; }
            }
          },
          { 
            field: "p99",   label: "p99" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p99.toFixed(2) ; }
            }
          },
          { 
            field: "p999",   label: "p999" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.p999.toFixed(2) ; }
            }
          },
          { 
            field: "stddev",   label: "stddev" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.stddev.toFixed(2) ; }
            }
          },
          { 
            field: "meanRate",   label: "meanRate" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.meanRate.toFixed(2) ; }
            }
          },
          { 
            field: "m1Rate",   label: "m1Rate" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m1Rate.toFixed(2) ; }
            }
          },
          { 
            field: "m5Rate",   label: "m5Rate" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m5Rate.toFixed(2) ; }
            }
          },
          { 
            field: "m15Rate",   label: "m15Rate" , toggled: true,
            custom: {
              getDisplay: function(bean) { return bean.m15Rate.toFixed(2) ; }
            }
          },
          { field: "durationUnits",   label: "Duration Units" , toggled: true},
          { field: "rateUnits",   label: "Rate Units" , toggled: true}
        ]
      }
    },

    onInit: function(options) {
      this.setBeans(options.timers) ;
    }
  });
  

  
  var UIMetric = UICollapsible.extend({
    label: "Metric", 
    config: {
      actions: [
        {
          action: 'refresh', label: 'Refresh',
          onClick: function (thisUI) {
            thisUI.onRefresh() ;
          }
        },
      ]
    },

    onInit: function(options) {
      this.memberName = options.memberName ;
      this.onRefresh() ;
    } ,

    onRefresh: function() {
      this.clear() ;

      var results = ClusterGateway.execute('server metric --member-name ' +  this.memberName) ;
      var metricRegistry = results[0].result.registry ;

      var asArray = function(map) {
        var holder = [] ;
        for(key in map) {
          var bean = map[key] ;
          bean.name = key ;
          holder.push(bean) ;
        }
        return holder ;
      };

      this.add(new UITimerMetric({ timers: asArray(metricRegistry.timers) })) ;
      this.add(new UICounterMetric({ counters: asArray(metricRegistry.counters) }), true) ;
      this.render() ;
    }
  }) ;
  
  return UIMetric ;
});
