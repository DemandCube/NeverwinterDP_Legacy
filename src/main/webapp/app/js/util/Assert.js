define([
  'jquery',
], function($) {
  Assert = {
    assertTrue: function(b, msg) {
      if(!b) {
        if(msg == null || msg == undefined) {
          throw new Error("expect a true value") ;
        }
        throw new Error(msg) ;
      }
    },
    
    assertFalse: function(b, msg) {
      if(b) {
        if(msg == null || msg == undefined) {
          throw new Error("expect a false value") ;
        }
        throw new Error(msg) ;
      }
    },
    
    assertNull: function(obj, msg) {
      if(obj != null) {
        if(msg == null || msg == undefined) {
          throw new Error("expect a null value") ;
        }
        throw new Error(msg) ;
      }
    },
    

    assertNotNull: function(obj, msg) {
      if(obj == null) {
        if(msg == null || msg == undefined) {
          throw new Error("expect a non null value") ;
        }
        throw new Error(msg) ;
      }
    },
    
    assertEquals: function(o1, o2, msg) {
      if(o1 !== o2) {
        if(msg == null || msg == undefined) {
          throw new Error("expect " + o1 + ", but result " + o2) ;
        }
        throw new Error(msg) ;
      }
    },
    
    fail: function(msg) {
      throw new Error(msg) ;
    }
  };

  return Assert ;
});

