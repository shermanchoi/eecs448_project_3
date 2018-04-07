let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?'), parent.document.URL.length);
ID = preID.slice(1);

//dummy
let title = "Title";
let author = "author";
let postContent = "asdfghj";


let originPost = document.getElementById("postandreplies");

let post = document.createElement("div");
post.setAttribute("id", "post");

let postT = document.createElement("div");
postT.setAttribute("id", "posttitle");

postT.innerHTML = title;

post.appendChild(postT);

let postA = document.createElement("div");
postA.setAttribute("id", "postauthor");

postA.innerHTML = "By: " + author;

post.appendChild(postA);

let postC = document.createElement("div");
postC.setAttribute("id", "postcontents");

let content = document.createElement("p");
content.innerHTML = postContent;

postC.appendChild(content);

post.appendChild(postC);

originPost.appendChild(post);

