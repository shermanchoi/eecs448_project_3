let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?postID='), parent.document.URL.length);
ID = preID.slice(8);

//create variable
let text = "";

let PnR = document.getElementById("postandreplies");

function postBody (text) {
    let obj = JSON.parse(text);

    let post = document.createElement("div");
    post.setAttribute("id", "post");

    let postT = document.createElement("div");
    postT.setAttribute("id", "posttitle");

    postT.innerHTML = obj.Title;

    post.appendChild(postT);

    let postA = document.createElement("div");
    postA.setAttribute("id", "postauthor");

    postA.innerHTML = "By: " + obj.Author;

    post.appendChild(postA);

    let postC = document.createElement("div");
    postC.setAttribute("id", "postcontents");

    let content = document.createElement("p");
    content.innerHTML = obj.Content;

    postC.appendChild(content);

    post.appendChild(postC);

    PnR.appendChild(post);
}

function formGenerate() {
    let formBody = document.createElement("div");
    formBody.setAttribute("id", "replyform");

    let form = document.createElement("form");
    form.setAttribute("action", "/postViewReply");
    form.setAttribute("method", "post");

    let fieldSet = document.createElement("fieldset");

    let formHead = document.createElement("div");
    formHead.setAttribute("class", "formheading");
    formHead.innerHTML = "Write a reply";

    fieldSet.appendChild(formHead);

    let formContent = document.createElement("div");
    formContent.setAttribute("class", "formcontent");

    let textBox = document.createElement("div");
    textBox.setAttribute("class", "textbox");
    textBox.setAttribute("id", "textbox");

    let textArea = document.createElement("textarea");
    textArea.setAttribute("name", "replycontent");
    textArea.setAttribute("id", "replycontent");
    sceditor.create(textArea, {format: 'bbcode', style: 'BBCode/Editor/sceditor-2.1.2/minified/themes/content/default.min.css'});

    textBox.appendChild(textArea);

    let IDhandler = document.createElement("input");
    IDhandler.setAttribute("type", "hidden");
    IDhandler.setAttribute("name", "postID");
    IDhandler.setAttribute("value", ID);

    textBox.appendChild(IDhandler);

    formContent.appendChild(textBox);

    fieldSet.appendChild(formContent);

    let buttons = document.createElement("div");
    buttons.setAttribute("class", "buttons");

    let cancelB = document.createElement("button");
    cancelB.setAttribute("type", "button");
    cancelB.setAttribute("class", "cancel");
    cancelB.setAttribute("onclick", "window.history.back()");
    cancelB.innerHTML = "Cancel";

    buttons.appendChild(cancelB);

    let submitB = document.createElement("input");
    submitB.setAttribute("type", "submit");
    submitB.setAttribute("value", "Reply");

    buttons.appendChild(submitB);

    fieldSet.appendChild(buttons);
    form.appendChild(fieldSet);
    formBody.appendChild(form);

    PnR.appendChild(formBody);
}

let xhttp = new XMLHttpRequest();

xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        postBody(this.responseText);
        formGenerate();
    }
};

xhttp.open("GET", "/api/post?postID="+ID, true);
xhttp.send();

