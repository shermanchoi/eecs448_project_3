let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?'), parent.document.URL.length);
ID = preID.slice(1);

let postTitle = "";
let postAuthor = "";
let postContent = "";

let text = "";
let obj = {};

//get necessary information from server
let xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if(this.readyState == 4 && this.status == 200) {
        text = this.responseText;
        obj = JSON.parse(text);
        alert(text);
    }
};

xhttp.open("GET", "/api/post?postid="+ID, true);
xhttp.send();

postTitle = obj.Title;
postAuthor = obj.Author;

let PoR = document.getElementById("postandreplies");

function createPostView (title, author) {
    //dummy for test
    /*
    title = "This is a meaningless post";
    author = "isNotaHuman";
    */

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

    PoR.appendChild(post);
}

createPostView(postTitle, postAuthor);


/*
function createReplyView () {
    let replyArea = document.createElement("div");
    replyArea.setAttribute("id", "replies");

    let replyHeader = document.createElement("div");
    replyHeader.setAttribute("id", "repliestitle");

    replyHeader.innerHTML = "Replies";

    let rHeaderButton = document.createElement("button");
    rHeaderButton.setAttribute("type", "button");
    rHeaderButton.setAttribute("class", "reply");
    rHeaderButton.innerHTML = "Write a Reply";

    replyHeader.appendChild(rHeaderButton);

    replyArea.appendChild(replyHeader);
    */

    //get all/some replies from server
    //create dummy for testing
    /*
    let replyNbr = 4;
    let replyAs = ["PppP", "o0Sherlock0o", "orioHERO", "turtleNinja"];
    let replyCs = ["asd", "1234568", "lDSBVZKJBVLAILURGBALJSKBDVLKJSBKDVsnjkfdb", "sdfgh<br>sfsndadfg<br>sdfbzdbr"];
    */
/*
    //create div.replycontent
    let replies = document.createElement("div");
    replies.setAttribute("class", "replycontent");
    //start loop to create reply blocks
    
    for(let i = 0; i < obj.Replies.length; i++) {
        let reply = document.createElement("div");
        reply.setAttribute("class", "reply");

        let replier = document.createElement("div");
        replier.setAttribute("class", "replier");

        replier.innerHTML = obj.Replies[i].Author;
        reply.appendChild(replier);

        replies.appendChild(reply);

        let replyContent = document.createElement("div");
        replyContent.setAttribute("class", "replycontent");
        replyContent.innerHTML = obj.Replies[i].Content;

        reply.appendChild(replyContent);

        replies.appendChild(reply);
    }
    replyArea.appendChild(replies);
    PoR.appendChild(replyArea);
}

createReplyView();
*/

//{"Title": "title", "Author": "author"}s
//{"Title": "title", "Author": "author", "Replies": [{"Author": "author1", "Content": "content1"}, {"Author": "author2", "Content": "content2"}]}