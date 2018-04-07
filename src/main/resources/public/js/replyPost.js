let textBox = document.getElementById("textbox");

let IDhandler = document.createElement("input");

IDhandler.setAttribute("type", "hidden");
IDhandler.setAttribute("name", "postID");
IDhandler.setAttribute("value", ID);

textBox.appendChild(IDhandler);