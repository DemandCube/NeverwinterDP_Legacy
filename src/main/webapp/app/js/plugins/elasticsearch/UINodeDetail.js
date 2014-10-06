define([
  'ui/UITable',
  'ui/UIBean',
  'ui/UICollapsible'
], function(UITable, UIBean, UICollapsible) {
  
  var UINodeInfo = UIBean.extend({
    label: 'Node Info',
    config: {
      beans : {
        nodeInfo : {
          label: 'Node Info',
          fields: [
            { label : 'Name', field: 'name' },
            { label : 'Version', field: 'version' },
            { label : 'Build', field: 'build' },
            { 
              label : 'Transport Bound Address', field: 'transport.bound_address' ,
              custom: {
                getDisplay: function(bean) { return  bean.transport.bound_address ; }
              }
            },
            { 
              label : 'Transport Publish Address', field: 'transport.publish_address' ,
              custom: {
                getDisplay: function(bean) { return  bean.transport.publish_address ; }
              }
            },
            { 
              label : 'Http Bound Address', field: 'http.bound_address' ,
              custom: {
                getDisplay: function(bean) { return  bean.http.bound_address ; }
              }
            },
            { 
              label : 'Http Publish Address', field: 'http.publish_address' ,
              custom: {
                getDisplay: function(bean) { return  bean.http.publish_address ; }
              }
            },
            { 
              label : 'Http Max Content Length', field: 'http.max_content_length_in_bytes' ,
              custom: {
                getDisplay: function(bean) { return  bean.http.max_content_length_in_bytes; }
              }
            }
          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('nodeInfo', options.nodeInfo) ;
      var beanState = this.getBeanState('nodeInfo');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    },
  });

  var UINodeSettings = UIBean.extend({
    label: 'Node Settings',
    config: {
      beans : {
        settings : {
          label: 'Settings',
          fields: [
            { label : 'Name', field: 'name' },
            { 
              label : 'Cluster Name', field: 'cluster.name',
              custom: {
                getDisplay: function(bean) { return  bean.cluster.name ; }
              }
            },
            { 
              label : 'Path Data', field: 'path.data',
              custom: {
                getDisplay: function(bean) { return  bean.path.data ; }
              }
            },
            { 
              label : 'Path Logs', field: 'path.logs',
              custom: {
                getDisplay: function(bean) { return  bean.path.logs ; }
              }
            }
          ]
        }
      }
    },

    onInit: function (options) {
      this.bind('settings', options.nodeInfo.settings) ;
      var beanState = this.getBeanState('settings');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    }
  });

  var UINodeOS = UIBean.extend({
    label: 'Node OS',
    config: {
      beans : {
        os: {
          label: 'OS',
          fields: [
            { label : 'Available Processors', field: 'available_processors' },
            { label : 'Refresh Interval In Millis', field: 'refresh_interval_in_millis' }
          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('os', options.nodeInfo.os) ;
      var beanState = this.getBeanState('os');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    }
  });

  var UINodeProcess = UIBean.extend({
    label: 'Node Process',
    config: {
      beans : {
        process: {
          label: 'Process',
          fields: [
            { label : 'Id', field: 'id' },
            { label : 'Refresh Interval In Millis', field: 'refresh_interval_in_millis' },
            { label : 'Max File Descriptor', field: 'max_file_descriptors' },
            { label : 'mlockall', field: 'mlockall' }
          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('process', options.nodeInfo.process) ;
      var beanState = this.getBeanState('process');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    }
  });
  

  var UIJVM = UIBean.extend({
    label: 'Node JVM',
    config: {
      beans : {
        jvm: {
          label: 'JVM',
          fields: [
            { label : 'Pid', field: 'pid' },
            { label : 'Version', field: 'version' },
            { label : 'VM Name', field: 'vm_name' },
            { label : 'VM Vesion', field: 'vm_version' },
            { label : 'VM Vendor', field: 'vm_vendor' },
            { label : 'Start time in millis', field: 'start_time_in_millis' },

            { 
              label : 'Mem Heap Init', field: 'mem.heap_init_in_bytes' ,
              custom: {
                getDisplay: function(bean) { return  bean.mem.heap_init_in_bytes ; }
              }
            },
            { 
              label : 'Mem Heap Max', field: 'mem.max_in_bytes', 
              custom: {
                getDisplay: function(bean) { return  bean.mem.heap_max_in_bytes ; }
              }
            },
            { 
              label : 'Mem Non Heap Init', field: 'mem.non_heap_init_in_bytes',
              custom: {
                getDisplay: function(bean) { return  bean.mem.non_heap_init_in_bytes ; }
              }
            },
            { 
              label : 'Mem Non Heap Max', field: 'mem.non-heap-max_in_bytes', 
              custom: {
                getDisplay: function(bean) { return  bean.mem.non_heap_max_in_bytes ; }
              }
            },
            { label : 'GC Collectors', field: 'gc_collectors' },
            { label : 'Memmory Pools', field: 'memory_pools' },

          ]
        }
      }
    },
    
    onInit: function (options) {
      this.bind('jvm', options.nodeInfo.jvm) ;
      var beanState = this.getBeanState('jvm');
      beanState.readOnly = true ;
      beanState.disableViewAction = options.disableViewAction ;
      return this ;
    }
  });
  
  var UINodeDetail = UICollapsible.extend({
    label: "Node Detail", 
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
      this.add(new UINodeInfo(options)) ;
      this.add(new UINodeSettings(options)) ;
      this.add(new UINodeOS(options)) ;
      this.add(new UINodeProcess(options)) ;
      this.add(new UIJVM(options)) ;
    }
  });
  
  return UINodeDetail;
});
