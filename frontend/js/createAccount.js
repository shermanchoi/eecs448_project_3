function generateDates() {
    var date_element = document.getElementById("Date");
    var test = date_element.innerHTML;
    for (int i = 1; i <= 31; i++) {
	date_element.innerHTML += "<option value=\"" + i + "\">" + i + "</option>\n";
    }
}
    
