define([
  'jquery'
], function($) {
  var CommonParameters = {
    memberSelector: [
      { name: "--member-role", description: "Select the target member by role", sample: "--membe-role generic" },
      { name: "--member-name", description: "Select the target member by member name", sample: "--member-name generic" },
      { name: "--member-uuid", description: "Select the target member by member uuid" },
    ]
  };

  return CommonParameters ;
});
