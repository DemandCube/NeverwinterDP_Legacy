define([
  'jquery', 
  'underscore', 
  'backbone',
  'ui/UITableTree'
], function($, _, Backbone, UITableTree) {
  var UITableTreeDemo = UITableTree.extend({
    label: "UITableTree Sample",

    config: {
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
          { field: "col1", label: "Col 1", toggled: true},
          { field: "col2", label: "Col 2", toggled: true},
          { 
            field: "customField",  label: "Custom Field",toggled: true,
            custom: {
              getDisplay: function(bean) {
                return bean.nestedObject == null ? null : bean.nestedObject.field ;
              },
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
  
  var uiTableDemo = new UITableTreeDemo() ;
  var node1 = uiTableDemo.addNode(
    { bean: "bean-1", col1: 'col 1', col2: 'col 2', nestedObject: { field: "nested field "} }
  );

  uiTableDemo.addNode(
    { bean: "bean-2", col1: 'col 1', col2: 'col 2', nestedObject: { field: "nested field "} }
  );

  var node1_1 = node1.addChild(
    { bean: "bean-1-1", col1: 'col 1', col2: 'col 2', nestedObject: { field: "nested field "} }
  );
  node1.addChild(
    { bean: "bean-1-2", col1: 'col 1', col2: 'col 2', nestedObject: { field: "nested field "} }
  );

  node1_1.addChild(
    { bean: "bean-1-1-1", col1: 'col 1', col2: 'col 2', nestedObject: { field: "nested field "} }
  );

  return uiTableDemo ;
});
