define([
  'jquery', 
  'underscore', 
  'backbone',
  "../../../js/libs/google-code-prettify/prettify",
  "css!../../../js/libs/google-code-prettify/prettify.css"
], function($, _, Backbone, prettify) {
  $.fn.prettify = function () { 
    this.html(prettyPrintOne(this.html())); 
  };

  var UIContent = Backbone.View.extend({
    initialize: function (config) {
      this.config = config ;
    },

    _template: _.template(
      "<div>" +
      "  <pre class='prettyprint <%=highlightSyntax%>'><%=config.content%></pre>" +
      "</div>"
    ),

    render: function() {
      var params = {
        config: this.config,
        highlightSyntax: ''
      };
      if(this.config.highlightSyntax != null) {
        params.highlightSyntax = 'lang-' + this.config.highlightSyntax ;
      }
      $(this.el).append(this._template(params));
      $(this.el).prettify();
    }
  });
  
  return UIContent ;
});
