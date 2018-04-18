let text = "";

function securityQuestionBlock(text) {
    alert(text);
    let obj = JSON.parser(text);
    
    let username = obj.username;
    let SQ = [obj.securityQuestion1, obj.securityQuestion2, obj.securityQuestion3];

    let formBody = document.getElementById("form");

    let form = document.createElement("form");
    form.setAttribute("action", "/forgotpassword/anwser");
    form.setAttribute("method", "post");

    let fieldset = document.createElement("fieldset");

    let formhead = document.createElement("div");
    formhead.setAttribute("class", "formheading");
    formhead.innerHTML = "Forgot Password --- Security Questions";
    fieldset.appendChild(formhead);

    let formsubhead = document.createElement("div");
    formsubhead.setAttribute("class", "formsubheading");
    formsubhead.innerHTML = " For User: ";

    let usernamestyle = document.createElement("span");
    usernamestyle.setAttribute("style", "font-size:90%;font-weight:bold;font-style:italic;");
    usernamestyle.innerHTML = username;
    formsubhead.appendChild(usernamestyle);

    fieldset.appendChild(formsubhead);

    let formContent = document.createElement("div");
    formContent.setAttribute("class", "formcontent");

    for(let i = 0; i < 3; i++) {
        let questionBox = document.createElement("div");
        questionBox.setAttribute("class", "textbox");

        let label = document.createElement("label");
        label.innerHTML = SQ[i];
        questionBox.appendChild(label);

        let linebreack = document.createElement("br");
        questionBox.appendChild(linebreack);

        let input = document.createElement("input");
        input.setAttribute("id", "sa"+i);
        input.setAttribute("name", "sa"+i);
        input.setAttribute("type", "text");
        questionBox.appendChild(input);

        formContent.appendChild(questionBox);
    }

    let buttons = document.createElement("div");
    buttons.setAttribute("class", "buttons");

    let cancelB = document.createElement("a");
    cancelB.setAttribute("href", "login.html");
    cancelB.innerHTML = "Cancel";
    buttons.appendChild(cancelB);

    let submitB = document.createElement("input");
    submitB.setAttribute("type", "submit");
    submitB.setAttribute("value", "Recover");
    buttons.appendChild(submitB);

    fieldset.appendChild(buttons);

    form.appendChild(fieldset);

    formBody.appendChild(form);
}

let xhttp = new XMLHttpRequest();

xhttp.onreadystatechange = function() {
    if(this.readyState == 4 && this.status == 200) {
        securityQuestionBlock(this.responseText);
    }
}

xhttp.open("get", "/api/security", true);
xhttp.send();