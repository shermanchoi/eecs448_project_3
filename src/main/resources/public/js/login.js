let error = parent.document.URL.substring(parent.document.URL.indexOf('?error='), parent.document.URL.length);
error = error.slice('?error='.length);

if(error == "loginError"){
	alert("Your username or password was incorrect, please try again.");
}else if(error == "banned"){
	alert("You are banned. You may not login.");
}
