// In case this doesn't work then check : https://stackoverflow.com/questions/1140402/how-to-add-jquery-in-js-file
$(document).ready(function () {
  $("#password").bind("cut copy paste", function (e) {
    e.preventDefault();
    alert("You cannot paste text into this textbox!");
    $("#password").bind("contextmenu", function (e) {
      e.preventDefault();
    });
  });
});
