define([
  'jquery',
], function($) {
  console.printJSON = function(obj) {
    console.log(JSON.stringify(obj, null, "  "));
  } ;

  return console ;
});

