let nav = document.getElementById("navbar");
function createNavButton() {
    let myaccB = document.createElement("a");
    myaccB.setAttribute("href", "accountMgmt.html");
    myaccB.innerHTML = "My Account";
    nav.appendChild(myaccB);

    let homeB = document.createElement("a");
    homeB.setAttribute("href", "main.html");
    homeB.innerHTML = "Home";
    nav.appendChild(homeB);

    let logOutB = document.createElement("a");
    logOutB.setAttribute("href", "/logout");
    logOutB.innerHTML = "Log Out";
    nav.appendChild(logOutB);
}

function createAdminB() {
        let adminButton = document.createElement("a");
        let adminText = document.createTextNode("Admin Mode");
        adminButton.appendChild(adminText);

        adminButton.classname = 'adminButton';
        adminButton.setAttribute('href', 'admin.html');
        adminButton.setAttribute('class', 'other');

        nav.appendChild(adminButton);
}

let xhttpNAV = new XMLHttpRequest();

xhttpNAV.onreadystatechange = function() {
    if(this.readyState == 4 && this.status == 200) {
        if(this.responseText == true){
            createAdminB();
        }
    } else {
        console.log(this.readyState + ", " + this.status);
    }
};

xhttpNAV.open("GET", "", true);
xhttpNAV.send();

createNavButton();