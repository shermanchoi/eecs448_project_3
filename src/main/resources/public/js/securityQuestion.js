let text = "";

function securityQuestionBlock(text) {
    alert(text);
    let obj = JSON.parse(text);
    
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


        let questionBox1 = document.createElement("div");
        questionBox1.setAttribute("class", "textbox");

        let label1 = document.createElement("label");
        label1.innerHTML = SQ[0];
        questionBox1.appendChild(label1);

        let linebreack1 = document.createElement("br");
        questionBox1.appendChild(linebreack1);

        let input1 = document.createElement("input");
        input1.setAttribute("id", "sa1");
        input1.setAttribute("name", "sa1");
        input1.setAttribute("type", "text");
        questionBox1.appendChild(input1);

        formContent.appendChild(questionBox1);

        let questionBox2 = document.createElement("div");
        questionBox2.setAttribute("class", "textbox");

        let label2 = document.createElement("label");
        label2.innerHTML = SQ[1];
        questionBox2.appendChild(label2);

        let linebreack2 = document.createElement("br");
        questionBox2.appendChild(linebreack2);

        let input2 = document.createElement("input");
        input2.setAttribute("id", "sa2");
        input2.setAttribute("name", "sa2");
        input2.setAttribute("type", "text");
        questionBox2.appendChild(input2);

        formContent.appendChild(questionBox2);

        let questionBox3 = document.createElement("div");
        questionBox3.setAttribute("class", "textbox");

        let label3 = document.createElement("label");
        label3.innerHTML = SQ[2];
        questionBox3.appendChild(label3);

        let linebreack3 = document.createElement("br");
        questionBox3.appendChild(linebreack3);

        let input3 = document.createElement("input");
        input3.setAttribute("id", "sa3");
        input3.setAttribute("name", "sa3");
        input3.setAttribute("type", "text");
        questionBox3.appendChild(input3);

        formContent.appendChild(questionBox3);

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
    else
    {
        console.log(this.readyState + ", " + this.status);
    }
}

xhttp.open("get", "/api/security", true);
xhttp.send();