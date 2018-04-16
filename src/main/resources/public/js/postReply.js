let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?postID='), parent.document.URL.length);
ID = preID.slice(8);

//dummy
let title = "";
let author = "";
let postContent = "";

let xhttp = new XMLHttpRequest();

xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        let text = this.responseText;
        let obj = JSON.parse(text);

        title = obj.Title;
        author = obj.Author;
        postContent = obj.postContent;


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
    }
};

xhttp.open("GET", "/api/post?postID="+ID, true);
xhttp.send();

