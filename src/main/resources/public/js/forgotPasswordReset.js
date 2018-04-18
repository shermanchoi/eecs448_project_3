function formValidate() {
    var errstatus = passwordValidate();
    switch (errstatus) {
	case 0:
	    return (true);
	    break;
	case 1:
	    alert("Password must be at least 8 characters");
	    return (false);
	    break;
	case 2:
	    alert("Passwords do not match");
	    return (false);
	    break;
    }
}

function passwordValidate() {
    var password = document.querySelectorAll('input[type="password"]')[0].value;
    var cpassword = document.querySelectorAll('input[type="password"]')[1].value;
    var errflag = 0;
    if (password.length < 8) {
	errflag = 1;
	document.getElementById('pwordalert').style.display = 'inline';
	document.getElementById('cpwordalert').style.display = 'none';
    }
    else if (cpassword != password) {
	errflag = 2;
	document.getElementById('cpwordalert').style.display = 'inline';
	document.getElementById('pwordalert').style.display = 'none';
    }
    else {
	document.getElementById('pwordalert').style.display = 'none';
	document.getElementById('cpwordalert').style.display = 'none';
    }

    return (errflag);
}
