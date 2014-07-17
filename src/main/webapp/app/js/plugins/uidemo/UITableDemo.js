define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UICollapsible',
  'ui/UIBean',
  'ui/UITable'
], function($, _, Backbone, UICollabsible, UIBean, UITable) {
  
  var MyUICollabsible = UICollabsible.extend({
    label: "Sample UICollapsible", 
    config: {
      actions: [
        { 
          action: "back", label: "Back",
          onClick: function(thisUI) {
            console.log("on click back") ;
          }
        },
        {
          action: "save", label: "Save",
          onClick: function(thisUI) {
            console.log("on click save") ;
          }
        }
      ]
    }
  }) ;
  
  var UITableDemo = UITable.extend({
    label: "UITable Sample",
    config: {
      toolbar: {
        dflt: {
          actions: [
            {
              action: "onNew", icon: "add", label: "New", 
              onClick: function(thisTable) { 
                thisTable.onAddBean() ;
              } 
            }
          ]
        }
      },
      
      bean: {
        label: 'Contact Bean',
        fields: [
          { 
            field: "bean",   label: "Bean", defaultValue: 'default value', 
            toggled: true, filterable: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              console.log('on click bean ' + JSON.stringify(bean)) ;
            }
          },
          {
            field:  "option", label: "Option", toggled: true, filterable: true,
            option: [
             {label: 'Option 1', value: 'opt1'},
             {label: 'Option 2', value: 'opt2'}
            ]
          },
          { 
            field: "multipleInput",  label: "Multiple Input", multiple: true,
            defaultValue: [ 'val1', 'val2' ], validator: { name: 'empty'},
            toggled: true
          },
          { 
            field: "textarea", label: "Text Area", type: "textarea", 
            filterable: false, toggled: true
          },
          { 
            field: "customField",  label: "Custom Field",toggled: true,
            onClick: function(thisTable, row) {
              var bean = thisTable.getItemOnCurrentPage(row) ;
              console.log('on click bean ' + JSON.stringify(bean)) ;
            },
            custom: {
              getDisplay: function(bean) {
                return bean.nestedObject == null ? null : bean.nestedObject.field ;
              },
              set: function(bean, obj) { bean.nestedObject = obj ;},
              
              autocomplete: {
                search: function(term, bean) {
                  console.log("search term: " + term + ', bean input = ' + bean.input) ;
                  var result = [
                    { value: { field: 'custom 1' }, label: "Custom(1)" },
                    { value: { field: 'custom 2' }, label: "Custom(2)" }
                  ];
                  return result ;
                }
              }
            }
          },
        ],
        actions:[
          {
            icon: "delete", label: "Delete",
            onClick: function(thisTable, row) { 
              thisTable.markDeletedItemOnCurrentPage(row) ;
              console.log('Mark delete row ' + row);
            }
          },
          {
            icon: "edit", label: "Mod",
            onClick: function(thisTable, row) { 
              thisTable.onEditBean(row) ;
            }
          }
        ]
      }
    }
  });
  
  var uiTableDemo = new UITableDemo() ;
  var beans = [] ;
  for(var i = 0; i < 151; i++) {
    var bean = {
      bean: "bean " + i,
      option: 'opt' + (i%2 + 1),
      multipleInput: ["input 1", "input 2"],
      textarea: "Textarea " + i,
      nestedObject: {
        field: "nested field " + i
      },
      peristableState: 'ORIGIN'
    };
    beans.push(bean) ;
  }
  uiTableDemo.setBeans(beans) ;
  
  return uiTableDemo ;
});
