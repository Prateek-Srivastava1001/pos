
function getLoginUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/login";
}

function getSignupUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/session/signup";
}

//BUTTON ACTIONS
function login(event){
	//Set the values to update
	var $form = $("#login-form");
	var formData = $form.serializeArray();
	var url = getLoginUrl();
    event.preventDefault(); // Prevent form submission

    // Get form data
    var username = $('#email').val();
    var password = $('#password').val();
    if(username==null ||username==""){
            dangerClick("Email cannot be empty");
            return true;
        }
    if(password==null ||password==""){
            dangerClick("Password cannot be empty");
            return true;
        }
    if(!ValidateEmail(formData[0].value)){
        return true;
    }
	$.ajax({
	   url: url,
	   type: 'POST',
	   data:{
           email: username,
           password: password
       },
	   success: function(response) {
            var redirectUrl = $("meta[name=baseUrl]").attr("content") + "/ui/home";
            window.location = redirectUrl;
	   },
	   error: function(response){
	    location.reload(true);
	   }
	});

	return false;
}

function signup(event){
	//Set the values to update
	var $form = $("#signup-form");
	var formData = $form.serializeArray();
	var url = getSignupUrl();
    event.preventDefault(); // Prevent form submission

    // Get form data
    var username = $('#email').val();
    var password = $('#password').val();
    if(username==null ||username==""){
        dangerClick("Email cannot be empty");
        return true;
    }
    if(password==null ||password==""){
        dangerClick("Password cannot be empty");
        return true;
    }
    if(!ValidateEmail(formData[0].value)){
        return true;
    }
	$.ajax({
	   url: url,
	   type: 'POST',
	   data:{
           email: username,
           password: password
       },
	   success: function(response) {
            var redirectUrl = $("meta[name=baseUrl]").attr("content") + "/ui/home";
            window.location = redirectUrl;
	   },
	   error: function(response){
	    location.reload(true);
	   }
	});

	return false;
}

function ValidateEmail(email) {

    // Regular expression pattern for email validation
    var pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!pattern.test(email)) {
    dangerClick("Invalid email address format!");
    return false
  }
  return true;
}
function togglePasswordVisibility() {
  var passwordField = document.getElementById("password");
  var toggleContainer = document.querySelector(".toggle-password");
  var toggleIcon = toggleContainer.querySelector(".toggle-icon");

  if (passwordField.type === "password") {
    passwordField.type = "text";
    toggleIcon.classList.remove("fa-eye");
    toggleIcon.classList.add("fa-eye-slash");
  } else {
    passwordField.type = "password";
    toggleIcon.classList.remove("fa-eye-slash");
    toggleIcon.classList.add("fa-eye");
  }
}
//INITIALIZATION CODE
function init(){
	$('#login').click(login);
	$('#signup').click(signup);
}

$(document).ready(init);

