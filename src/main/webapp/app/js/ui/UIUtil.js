define([
  'jquery'
], function($) {
  var INT_REGEX = /^-{0,1}\d+$/ ;
  var FLOAT_REGEX = /^-{0,1}\d*\.{0,1}\d+$/;
  var EMAIL_REGEX = /^\S+@\S+\.\S+$/ ;
  
  var Validator = {
    empty: function(validator, value) {
      if(value == null || value == '') {
        if(validator.errorMsg != null) throw validator.errorMsg ;
        throw "The value cannot be null or empty";
      }
      return value ;
    },
    
    integer: function(validator, value) {
      var message = validator.errorMsg != null ? validator.errorMsg : "Expect an integer value" ; 
      if(value == null) throw message;
      var intVal = 0;
      if (typeof value == 'string') {
        if(INT_REGEX.test(value)) intVal = parseInt(value) ;
        else throw message;
      } else {
        intVal = value ;
      }
      if(isNaN(intVal)) throw message ;
      
      if(validator.from != null && intVal < validator.from) {
        throw "the value need to be greater than or equal to " + validator.from ;
      }
      if(validator.to != null && intVal > validator.to) {
        throw "the value need to be less than or equal to " + validator.to ;
      }
      return intVal ;
    },
    
    number: function(validator, value) {
      var message = validator.errorMsg != null ? validator.errorMsg : "Expect an integer value" ;
      if(value == null) throw message;
      var floatVal = 0;
      if (typeof value == 'string') {
        if(FLOAT_REGEX.test(value)) floatVal = parseFloat(value) ;
        else throw message;
      } else {
        floatVal = value ;
      }
      if(isNaN(floatVal)) throw message ;
      
      if(validator.from != null && floatVal < validator.from) {
        throw "the value need to be greater than or equal to " + validator.from ;
      }
      if(validator.to != null && floatVal > validator.to) {
        throw "the value need to be less than or equal to " + validator.to ;
      }
      return floatVal ;
    },
    
    email: function(validator, value) {
      if(value == null || value.length == 0) return value ;
      var message = validator.errorMsg != null ? validator.errorMsg : "Expect a valid email address in the form name@host.domain" ;
      if(EMAIL_REGEX.test(value)) return value ;
      throw message ;
    }
  };

  var UIUtil = {
    Validator: Validator,

    getAncestorOfType: function(uicomp, type) {
      var uiParent = uicomp.uiParent ;
      while(uiParent != null) {
        if(type == uiParent.type) return uiParent ;
        uiParent = uiParent.uiParent ;
      }
      return null ;
    }
  }
 
  return UIUtil ;
});
