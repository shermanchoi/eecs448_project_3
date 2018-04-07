let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?'), parent.document.URL.length);
ID = preID.slice(1);

let postTitle = "";
let postAuthor = "";
let postContent = "";

//get necessary information from server


let PoR = document.getElementById("postandreplies");

function createPostView (title, author) {
    //dummy for test
    title = "This is a meaningless post";
    author = "isNotaHuman";
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

    //get all/some replies from server
    //create dummy for testing
    let replyNbr = 4;
    let replyAs = ["PppP", "o0Sherlock0o", "orioHERO", "turtleNinja"];
    let replyCs = ["asd", "1234568", "lDSBVZKJBVLAILURGBALJSKBDVLKJSBKDVsnjkfdb", "sdfgh<br>sfsndadfg<br>sdfbzdbr"];

    //create div.replycontent
    let replies = document.createElement("div");
    replies.setAttribute("class", "replycontent");
    //start loop to create reply blocks
    
    for(let i = 0; i < replyNbr; i++) {
        let reply = document.createElement("div");
        reply.setAttribute("class", "reply");

        let replier = document.createElement("div");
        replier.setAttribute("class", "replier");

        replier.innerHTML = replyAs[i];
        reply.appendChild(replier);

        replies.appendChild(reply);

        let replyContent = document.createElement("div");
        replyContent.setAttribute("class", "replycontent");
        replyContent.innerHTML = replyCs[i];

        reply.appendChild(replyContent);

        replies.appendChild(reply);
    }
    replyArea.appendChild(replies);
    PoR.appendChild(replyArea);
}

createReplyView();


/*
obj.keyName; //single item

obj.keyName.subKey;//
*/