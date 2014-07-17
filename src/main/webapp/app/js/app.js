define([
  'jquery',
  'util/console',
  'site/UIBanner',
  'site/UIFooter',
  'site/UINavigation',
  'site/UIWorkspace',
], function($, console, UIBanner, UIFooter, UINavigation, UIWorkspace) {

  var app = {
    view : {
      UIBanner: new UIBanner(),
      UINavigation: new UINavigation(),
      UIWorkspace: UIWorkspace,
      UIFooter: new UIFooter(),
    },

    initialize: function() {
      console.log("start initialize app") ;
      this.render() ;
      console.log("finish initialize app") ;
    },

    render: function() {
      this.view.UIBanner.render() ;
      this.view.UINavigation.render() ;
      this.view.UIWorkspace.render() ;
      this.view.UIFooter.render() ;
    },

    reload: function() {
      window.location = ROOT_CONTEXT + "/index.html" ;
    }
  } ;
  
  return app ;
});
