ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");


var ClusterShell = {
  server: {
    install: function(params) {
      cluster.ClusterGateway.module.install({
        params: params,

        onResponse: function(resp) {
          if(!resp.success) { 
            console.printJSON(resp);
          }
          for(var i = 0; i < resp.results.length; i++) {
            var result = resp.results[i];
            var printer = new cluster.ModuleRegistrationPrinter(console, result.fromMember, result.result);
            printer.printModuleRegistration() ;
          }
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    },

    uninstall: function(params) {
      cluster.ClusterGateway.module.uninstall({
        params: params,

        onResponse: function(resp) {
          console.h1("Uninstall the module Zookeeper on the zookeeper role servers") ;
          for(var i = 0; i < resp.results.length; i++) {
            var result = resp.results[i];
            var printer = new cluster.ModuleRegistrationPrinter(console, result.fromMember, result.result);
            printer.printModuleRegistration() ;
          }
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    },

    metric: function(params) {
      cluster.ClusterGateway.server.metric({
        params: params,

        onResponse: function(resp) {
          for(var i = 0; i < resp.results.length; i++) {
            var result = resp.results[i];
            var printer = new cluster.MetricPrinter(console, result.fromMember, result.result);
            printer.printTimer();
          }
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    },

    clearMetric: function(params) {
      params = params != null ? params : {"expression": "*" } ;
      cluster.ClusterGateway.server.clearMetric({
        params: params,

        onResponse: function(resp) {
          console.h1("Clear metric monitor " + params["expression"]) ;
          new cluster.ResponsePrinter(console, resp).print() ;
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    }
  },

  module: {
    list: function(params) {
      if(params == undefined) {
        params = {"type": "available" } ;
      }

      cluster.ClusterGateway.module.list({
        params: params,

        onResponse: function(resp) {
          console.h1("List the current module status of all the servers") ;
          for(var i = 0; i < resp.results.length; i++) {
            var result = resp.results[i];
            var printer = new cluster.ModuleRegistrationPrinter(console, result.fromMember, result.result);
            printer.printModuleRegistration() ;
          }
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    }
  }
}
