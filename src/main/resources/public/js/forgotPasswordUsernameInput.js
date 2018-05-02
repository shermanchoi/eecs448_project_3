let error = parent.document.URL.substring(parent.document.URL.indexOf('?error='), parent.document.URL.length);
error = error.slice('?error='.length);

if(error == "notFound"){
	alert("We could not find any accounts with that username.");
}
