let postT = "Some Title";
let postA = "Author";
let postR = 0;
let postID = 0;

let xhttp = new XMLHttpRequest();



                


//transfer posts data to a JS array of arrays of strings and numbers
//For ex.
//posts = [IDs = ['1234', '6543', '8630', ...], titles = ['title1', 'title2', ...], authors = ['author1', 'author2', ...], replies = [1, 213, 33, ...]]
//for testing, create some dummy arrays
/*
let IDs = ["0", "123", "333"];
let titles = ["What's the point?", "This is an empty post", "Aaaaaughibbrgubugbugrguburgle!"];
let authors = ["AcePilot", "Null", "Murloc123"];
let replies = [2, 0, 18359];
let posts = [IDs, titles, authors, replies];
*/
                
function createList () {
    //get div.forumtable from html
    let postList = document.getElementById("forumTable");
    //create table
    let postTable = document.createElement("table");

    //create thead and tbody
    let postHeader = document.createElement("thead");
    let postBody = document.createElement("tbody");

    //create tr and th
    let tr = document.createElement("tr");
    let titles = document.createElement("th");
    let rep = document.createElement("th");

    //set th's classes
    titles.setAttribute("class", "titles");
    rep.setAttribute("class", "replies");

    //set value in th's
    titles.innerHTML = "Post";
    rep.innerHTML = "Replies";

    //insert th's into tr
    tr.appendChild(titles);
    tr.appendChild(rep);

    //insert tr into thead
    postHeader.appendChild(tr);

    //insert thead into table
    postTable.appendChild(postHeader);

    xhttp.onreadystatechange = function() {
        //get posts from server
        if(this.readyState == 4 && this.status == 200) {
            let text = this.responseText;
            //parse json
            let postL = JSON.parse(text);
            //start create post blocks
            for(let i = 0; i < postL.Posts.length; i++) {
                //create tr
                let post = document.createElement("tr");
                let oore = document.createAttribute("class");
                if(i%2 == 0) {
                    oore.value = "odd";
                } else {
                    oore.value = "even";
                }
                //set tr's class
                post.setAttributeNode(oore);
        
                //get post info.
                postT = postL.Posts[i].Title;
                postA = postL.Posts[i].Author;
                postR = postL.Posts[i].Reply;
                postID = postL.Posts[i].ID;
        
                //create td for title and author name
                let mainBlock = document.createElement("td");
                //set td's class
                mainBlock.setAttribute("class", "mainpostinfo");
        
                //create div's for title and author
                let titleBlock = document.createElement("div");
                //set class for title
                titleBlock.setAttribute("class", "posttitle");
        
                let authorBlock = document.createElement("div");
                //set class for author
                authorBlock.setAttribute("class", "postauthor");
        
                //create a for title
                let _postT = document.createElement("a");
        
                //set value in title
                _postT.innerHTML = postT;
                //set value in author
                authorBlock.innerHTML = postA;
        
                //set click action on title
                let postlink = "postView.html?" + postID;
                _postT.setAttribute("href", postlink);//go to post detail page
        
                //insert the title link to div
                titleBlock.appendChild(_postT);
        
                //insert title and author to td
                mainBlock.appendChild(titleBlock);
                mainBlock.appendChild(authorBlock);
        
                //create td for replies
                let replieBlock = document.createElement("td");
                //set class for replies
                replieBlock.setAttribute("class", "replies");
        
                //set value for replies
                replieBlock.innerHTML = postR;
        
                //insert main block td into tr
                post.appendChild(mainBlock);
                //insert reply block td into tr
                post.appendChild(replieBlock);
                //insert tr into tbody
                postBody.appendChild(post);
            }
        }
    };
    
    xhttp.open("GET", "/api/posts", true);
    xhttp.send();


    //insert tbody into table
    postTable.appendChild(postBody);
    //insert table to div.forumTable
    postList.appendChild(postTable);
}

createList();