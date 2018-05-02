let error = parent.document.URL.substring(parent.document.URL.indexOf('?error='), parent.document.URL.length);
error = error.slice('?error='.length);

if(error == "failure"){
	alert("Account creation failure. One of these things happened:\n-Your username was already taken. Select another one.\n-You tried to make an account with invalid information. Please check your information and follow directions.");
}else if(error = "shortPassword"){
	alert("Your password was too short.");
}

function generateDates() {
    var today = new Date();
    var year = today.getFullYear();

    var date_element = document.getElementById("Date");
    var new_date;
    var date_text;
    for (var i = 1; i <= 31; i++) {
	new_date = document.createElement("option");
	new_date.setAttribute("value", i);
	date_text = document.createTextNode(i);

	new_date.appendChild(date_text);
	date_element.appendChild(new_date);	
    }

    var year_element = document.getElementById("Year");
    var new_year;
    var year_text;
    for (var i = (year - 100); i <= year; i++) {
	new_year = document.createElement("option");
	new_year.setAttribute("value", i);
	year_text = document.createTextNode(i);

	new_year.appendChild(year_text);
	year_element.appendChild(new_year);

	if (i == (year - 18)) {
	    new_year.setAttribute("selected", "1");
	}
    }
}

function formValidate() {
    var errstatus = textBoxValidate() + dateValidate() + passwordValidate() + securityValidate();
    if (errstatus) {
	alert("Some parts of the form missing or invalid; please check form");
	return (false);
    }
}

function textBoxValidate() {
    var textboxes = document.querySelectorAll('input[type="text"]');
    var errflag = 0;

    for (var i = 0; i < textboxes.length; i++) {
	if (textboxes[i].value == '') {
	    document.getElementById('tb' + i + 'alert').style.display = "inline";
	    errflag = 1;
	}
	else {
	    document.getElementById('tb' + i + 'alert').style.display = "none";
	}
    }

    return(errflag);
}    

function dateValidate() {
    var month = document.getElementById('Month').value;
    var date = document.getElementById('Date').value;
    var datealert = document.getElementById('datealert');
    var invalid_date = (month == 2 && date > 28) || (month % 2 == 0 && month <= 7 && date > 30) || (month % 2 == 1 && month > 7 && date > 30);

    if (invalid_date) {
	datealert.style.display = 'inline';
	return (1);
    }
    else {
	datealert.style.display = 'none';
	return (0);
    }
}

function passwordValidate() {
    var password = document.querySelectorAll('input[type="password"]')[0].value;
    var cpassword = document.querySelectorAll('input[type="password"]')[1].value;
    var errflag = 0;
    if (password.length < 8) {
	errflag = 1;
	document.getElementById('pwordalert').style.display = 'inline';
    }
    else {
	document.getElementById('pwordalert').style.display = 'none';
    }
    if (cpassword != password) {
	errflag = 1;
	document.getElementById('cpwordalert').style.display = 'inline';
    }
    else {
	document.getElementById('cpwordalert').style.display = 'none';
    }

    return (errflag);
}

function securityValidate() {
    var sq1 = document.getElementById("sq1").value;
    var sq2 = document.getElementById("sq2").value;
    var sq3 = document.getElementById("sq3").value;
    var sq1alert = document.getElementById("sq1alert");
    var sq2alert = document.getElementById("sq2alert");
    var sq3alert = document.getElementById("sq3alert");
    var errflag = 0;

    if (sq1 == sq2 && sq1 == sq3) {
	sq1alert.style.display = "inline";
	sq2alert.style.display = "inline";
	sq3alert.style.display = "inline";
	errflag = 1;
    }
    else if (sq1 == sq2) {
	sq1alert.style.display = "inline";
	sq2alert.style.display = "inline";
	sq3alert.style.dsipaly = "none";
	errflag = 1;
    }
    else if (sq1 == sq3) {
	sq1alert.style.display = "inline";
	sq2alert.style.display = "none";
	sq3alert.style.display = "inline";
	errflag = 1;
    }
    else if (sq2 == sq3) {
	sq1alert.style.display = "none";
	sq2alert.style.display = "inline";
	sq3alert.style.display = "inline";
	errflag = 1;
    }
    else {
	sq1alert.style.display = "none";
	sq2alert.style.display = "none";
	sq3alert.style.display = "none";
    }

    return (errflag);
}
