define([
  'service/ClusterGateway',
  'ui/UIPopup',
  'ui/UITable',
  'ui/UIBean',
  'ui/UICollapsible',
  'ui/UIContent',
], function(ClusterGateway, UIPopup, UITable, UIBean, UICollapsible, UIContent) {
  
  var UIMemoryMonitor = UIBean.extend({
    label: 'Memory Info',
    config: {
      beans : {
        memory : {
          label: 'Memory Info',
          fields: [
            { label : 'Init', field: 'init' },
            { label : 'Committed', field: 'committed' },
            { label : 'Max', field: 'max' },
            { label : 'Use', field: 'use' },
          ],
          edit: {
            disable: true
          } ,
          view: {
            actions: [
              {
                action:'more', label: "More", icon: "grid",
                onClick: function(thisInfo) {
                  var memDetail = new UIMemoryMonitorDetail({jvmInfo: thisInfo.jvmInfo}) ;
                  var uiBreadcumbs = thisInfo.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(memDetail) ;
                }
              }
            ]
          }
        }
      }
    },
    
    onInit: function (options) {
      this.jvmInfo = options.jvmInfo ;
      this.bind('memory', this.jvmInfo.memoryInfo) ;
      var beanState = this.getBeanState('memory');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    },
  });
  
  var UIMemoryMonitorList = UITable.extend({
    label: 'Memory Detail',
    config: {
      toolbar: {
        dflt: {
          actions: []
        }
      },
      bean: {
        fields: [
          { label : 'Name', field : 'name', toggled : true, filterable : true},
          { label : 'Pool Names', field : 'poolNames', toggled : true, filterable : true},
          { label : 'Collection Count', field : 'collectionCount', toggled : true, filterable : true},
          { label : 'Collection Time', field : 'collectionTime', toggled : true, filterable : true}
        ],
        actions: []
      }
    },
    
    init: function (garbageCollectorInfo) {
      this.setBeans(garbageCollectorInfo) ;
      return this ;
    }
  });
  
  var UIMemoryMonitorDetail = UICollapsible.extend({
    label: "Memory", 
    config: {
      actions: [
        {
          action: 'back', label: 'Back',
          onClick: function (thisUI) {
            var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
            uiBreadcumbs.back() ;
          }
        },
      ]
    },
    
    onInit: function(options) {
      this.add(new UIMemoryMonitor({jvmInfo: options.jvmInfo, disableViewAction: true})) ;
      this.add(new UIMemoryMonitorList().init(options.jvmInfo.garbageCollectorInfo)) ;
    }
  });
  
  var UIThreadMonitor = UIBean.extend({
    label: 'Thread Info',
    config: {
      beans : {
        threadInfo : {
          label: 'Thread Info',
          fields: [
            { label : 'Thread Started Count', field: 'threadStartedCount'},
            { label : 'Thread Peak Count', field: 'threadPeakCount'},
            { label : 'Thread Count', field: 'threadCount'},
            { label : 'Thread Deamon Count', field: 'threadDeamonCount'}
          ],
          view: {
            actions: [
              {
                action:'more', label: "More", icon: "grid",
                onClick: function(thisInfo) {
                  var threadDetail = new UIThreadMonitorDetail({threadInfo: thisInfo.threadInfo}) ;
                  var uiBreadcumbs = thisInfo.getAncestorOfType('UIBreadcumbs') ;
                  uiBreadcumbs.push(threadDetail) ;
                }
              }
            ]
          }
        }
      }
    },
    
    onInit: function (options) {
      this.threadInfo = options.threadInfo ;
      this.bind('threadInfo', options.threadInfo) ;
      var beanState = this.getBeanState('threadInfo') ;
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
    }
  });
  
  var UIThreadMonitorList = UITable.extend({
    label: 'Threads',
    config: {
      toolbar: {
        dflt: {
          actions: []
        }
      },
      bean: {
        fields: [
          { label : 'Id', field : 'threadId', toggled : true },
          { label : 'Name', field : 'threadName', toggled : true, filterable : true },
          { label : 'Thread Block Count', field : 'threadBlockCount', toggled : true },
          { label : 'Thread Block Time', field : 'threadBlockTime', toggled : true },
          { label : 'Thread Waited Count', field : 'threadWaitedTime', toggled : true },
          { label : 'Thread State', field : 'threadState', toggled : true },
          { label : 'Thread CPU Time', field : 'threadCPUTime', toggled : true },
          { label : 'Thread User Time', field : 'threadUserTime', toggled : true },
        ],
        actions: [
          {
            icon: "grid", label: "Stacktrace",
            onClick: function(thisUI, row) {
              var thread = thisUI.getItemOnCurrentPage(row) ;
              var uiContent = new UIContent({content: thread.threadStackTrace}) ;
              var popupConfig = {
                title: "Thread Stacktrace", 
                minWidth: 800, 
                maxHeight: 600, 
                modal: true
              } ;
              UIPopup.activate(uiContent, popupConfig) ;
            }
          }
        ]
      }
    },
    
    onInit: function (options) {
      this.setBeans(options.threadInfo.threadInfos) ;
    }
  });
  
  var UIThreadMonitorDetail = UICollapsible.extend({
    label: "Threads Detail", 
    config: {
      actions: [
        {
          action: 'back', label: 'Back',
          onClick: function (thisUI) {
            var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
            uiBreadcumbs.back() ;
          }
        },
      ]
    },
    
    onInit: function(options) {
      this.add(new UIThreadMonitor({threadInfo: options.threadInfo, disableViewAction: true})) ;
      this.add(new UIThreadMonitorList({threadInfo: options.threadInfo})) ;
    }
  });
  
  var UIJVMInfo = UICollapsible.extend({
    label: "JVM Info",
    config: {
      actions: [
        {
          action: 'refresh', label: 'Refresh',
          onClick: function (thisUI) {
            thisUI.onRefresh() ;
          }
        },
        {
          action: 'back', label: 'Back',
          onClick: function (thisUI) {
            var uiBreadcumbs = thisUI.getAncestorOfType('UIBreadcumbs') ;
            uiBreadcumbs.back() ;
          }
        },
      ]
    },

    onInit: function(options) {
      this.memberName = options.memberName ;
      this.label = "JVM Info(" + this.memberName + "')"; 
      this.onRefresh() ;
    },

    onRefresh: function() {
      this.clear() ;

      console.log('member name: ' + this.memberName) ;
      var results = ClusterGateway.execute('server jvminfo --member-name ' + this.memberName) ;
      this.jvmInfo = results[0].result ;

      this.add(new UIMemoryMonitor({ jvmInfo: this.jvmInfo })) ;
      this.add(new UIThreadMonitor({ threadInfo: this.jvmInfo.threadInfo })) ;
      this.render() ;
    }
    
  });
  
  return UIJVMInfo;
});
