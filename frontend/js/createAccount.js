function generateDates() {
    var today = new Date();
    var year = today.getFullYear();

    var date_element = document.getElementById("Date");
    for (var i = 1; i <= 31; i++) {
	date_element.innerHTML += "<option value=\"" + i + "\">" + i + "</option>\n";
    }

    var year_element = document.getElementById("Year");
    for (var i = (year - 100); i <= year; i++) {
	year_element.innerHTML += '<option value=\"' + i + '\">' + i + '</option>\n';
    }
}

function formValidate() {
    var errstatus = textBoxValidate() || dateValidate() || passwordValidate() || securityValidate();
    if (errstatus) {
	alert("Some parts of the form missing or invalid; please check form");
	return (false);
    }
}

function textBoxValidate() {
    var textboxes = document.querySelectorAll('input[type="text"]');
    var tbalert;
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
