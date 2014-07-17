var ROOT_CONTEXT = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/"));

var JSLIBS = "../../js/libs" ;

require.config({
  urlArgs: "bust=" + (new Date()).getTime(), //prevent cache for development
  baseUrl: 'js',
  waitSeconds: 60,
  
  paths: {
    jquery:       JSLIBS + '/jquery/jquery',
    jqueryui:     JSLIBS + '/jquery/jquery-ui-1.11.0/jquery-ui',
    underscore:   JSLIBS + '/underscore/underscore-1.5.2',
    backbone:     JSLIBS + '/backbonejs/backbonejs-1.1.0',
  },
  
  shim: {
    jquery: {
      exports: '$'
    },
    jqueryui: {
      deps: ["jquery"],
      exports: "jqueryui"
    },
    underscore: {
      exports: '_'
    },
    backbone: {
      deps: ["underscore", "jquery"],
      exports: "Backbone"
    }
  }
});

require([
  'jquery', 'app'
], function($, App){
  app = App ;
  app.initialize() ;
});
