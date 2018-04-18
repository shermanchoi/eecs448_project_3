let postID = "";
let preID = parent.document.URL.substring(parent.document.URL.indexOf('?postID='), parent.document.URL.length);
ID = preID.slice(8);

let postTitle = "";
let postAuthor = "";
let postContent = "";

let text = "";
let obj = {};


let PoR = document.getElementById("postandreplies");

//get necessary information from server
let xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if(this.readyState == 4 && this.status == 200) {
        text = this.responseText;
        obj = JSON.parse(text);

        postTitle = obj.Title;
        postAuthor = obj.Author;
        postContent = obj.Content;

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

        let replyArea = document.createElement("div");
        replyArea.setAttribute("id", "replies");

        let replyHeader = document.createElement("div");
        replyHeader.setAttribute("id", "repliestitle");

        replyHeader.innerHTML = "Replies";

        let rHeaderButton = document.createElement("button");
        rHeaderButton.setAttribute("type", "button");
        rHeaderButton.setAttribute("class", "reply");
        let link = "'/postViewReply?postID=" + ID + "'";
        let replyLink = "window.location="+link;
        rHeaderButton.setAttribute("onclick", replyLink);
        rHeaderButton.innerHTML = "Write a Reply";

        replyHeader.appendChild(rHeaderButton);

        replyArea.appendChild(replyHeader);

        PoR.appendChild(replyArea);
    }
};

xhttp.open("GET", "/api/post?postID="+ID, true);
xhttp.send();








let text2 = "";
let obj2 = {};

let xhttp2 = new XMLHttpRequest();

xhttp2.onreadystatechange = function () {
    if(this.readyState == 4 && this.status == 200) {
       

        //dummy for testing
        /*
        let replyNbr = 4;
        let replyAs = ["PppP", "o0Sherlock0o", "orioHERO", "turtleNinja"];
        let replyCs = ["asd", "1234568", "lDSBVZKJBVLAILURGBALJSKBDVLKJSBKDVsnjkfdb", "sdfgh<br>sfsndadfg<br>sdfbzdbr"];
        */

        text2 = this.responseText;
        obj2 = JSON.parse(text2);
        //alert(text2);

        //create div.replycontent
        let replies = document.createElement("div");
        replies.setAttribute("class", "replycontent");
        //start loop to create reply blocks
        
        for(let i = 0; i < obj2.Replies.length; i++) {
            let reply = document.createElement("div");
            reply.setAttribute("class", "reply");

            let replier = document.createElement("div");
            replier.setAttribute("class", "replier");

            replier.innerHTML = obj2.Replies[i].Author;
            reply.appendChild(replier);

            replies.appendChild(reply);

            let replyContent = document.createElement("div");
            replyContent.setAttribute("class", "replycontent");
            replyContent.innerHTML = obj2.Replies[i].Content;

            reply.appendChild(replyContent);

            replies.appendChild(reply);
        }
        replyArea.appendChild(replies);
        PoR.appendChild(replyArea);
    }
    else {
        console.log(this.readyState, ", " , this.status);
    }
}

xhttp2.open("GET", "/api/postReply?postID="+ID, true);
xhttp2.send();




//{"Title": "title", "Author": "author", "Content": "content"}


//{Replies": [{"Author": "author1", "Content": "content1"}, {"Author": "author2", "Content": "content2"}]}

//{"currentP": 1, "totalP": 10, "Replies": [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}]}

//{"currentP": 1, "totalP": 10, "Posts": [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}]}