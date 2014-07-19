ScriptRunner.require("classpath:util/io.js");
ScriptRunner.require("classpath:util/assert.js");
ScriptRunner.require("classpath:cluster/cluster.js");


var ClusterShell = {
  server: {
    metric: function(command) {
      cluster.ClusterGateway.execute({
        command: command,

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

    metricClear: function(command) {
      cluster.ClusterGateway.execute({
        command: command,

        onResponse: function(resp) {
          console.h1("Clear metric monitor " + command) ;
          new cluster.ResponsePrinter(console, resp).print() ;
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    }
  },

  module: {
    list: function(command) {
      cluster.ClusterGateway.execute({
        command: command,

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
    },

    install: function(command) {
      cluster.ClusterGateway.execute({
        command: command,

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

    uninstall: function(command) {
      cluster.ClusterGateway.execute({
        command: command,

        onResponse: function(resp) {
          console.h1("Uninstall by command " + command) ;
          for(var i = 0; i < resp.results.length; i++) {
            var result = resp.results[i];
            var printer = new cluster.ModuleRegistrationPrinter(console, result.fromMember, result.result);
            printer.printModuleRegistration() ;
          }
          Assert.assertTrue(resp.success && !resp.isEmpty()) ;
        }
      }) ;
    },
  }
}
