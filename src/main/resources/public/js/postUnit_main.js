let xhttp = new XMLHttpRequest();

let postL = {};

                


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

    let text = "";

    function createPostBlock(postL) {
        //start create post blocks
        for(let i = 0; i < 20; i++) {
            //create tr
            let post[i] = document.createElement("tr");
            let oore[i] = document.createAttribute("class");
            if(i%2 == 0) {
                oore[i].value = "odd";
            } else {
                oore[i].value = "even";
            }
            //set tr's class
            post[i].setAttributeNode(oore);
        
            //create td for title and author name
            let mainBlock[i] = document.createElement("td");
            //set td's class
            mainBlock[i].setAttribute("class", "mainpostinfo");
        
            //create div's for title and author
            let titleBlock[i] = document.createElement("div");
            //set class for title
            titleBlock[i].setAttribute("class", "posttitle");
        
            let authorBlock[i] = document.createElement("div");
            //set class for author
            authorBlock[i].setAttribute("class", "postauthor");

            let authorLink[i] = document.createElement("a");
            authorLink[i].setAttribute("href", "/profile?username="+postL.Posts[i].Author);
            authorLink[i].innerHTML = postL.Posts[i].Author;
            authorBlock[i].appendChild(authorLink);
        
            //create a for title
            let _postT[i] = document.createElement("a");
        
            //set value in title
            _postT[i].innerHTML = postL.Posts[i].Title;
        
            //set click action on title
            let postlink[i] = "post?postID=" + postL.Posts[i].ID;
            _postT[i].setAttribute("href", postlink);//go to post detail page
        
            //insert the title link to div
            titleBlock[i].appendChild(_postT[i]);
        
            //insert title and author to td
            mainBlock[i].appendChild(titleBlock[i]);
            mainBlock[i].appendChild(authorBlock[i]);
        
            //create td for replies
            let replieBlock[i] = document.createElement("td");
            //set class for replies
            replieBlock[i].setAttribute("class", "replies");
        
            //set value for replies
            replieBlock[i].innerHTML = postL.Posts[i].Reply;
        
            //insert main block td into tr
            post[i].appendChild(mainBlock[i]);
            //insert reply block td into tr
            post[i].appendChild(replieBlock[i]);
            //insert tr into tbody
            postBody.appendChild(post[i]);
        }
    }

    function indexBar(currentPg, totalPg) {
        const PROXIMATE = 3;
        const LEFT_NUM = 1;
        const RIGHT_NUM = totalPg;
        var current_page = currentPg; //TODO: Determine a way to get the current page
    
        var indexBar = document.getElementById("indexbar"); //Get the indexbar div
    
        /*
        * MAIN COMPONENT CONSTRUCTION
        */
    
        var leftArrowAnchor = document.createElement("a");
        leftArrowAnchor.setAttribute('id', 'leftarrowanchor');
        leftArrowAnchor.setAttribute('onclick', 'updatePostBlock(postL, '+(current_page-1)+');'); 
        leftArrowAnchor.innerHTML = "<<";
    
        var rightArrowAnchor = document.createElement("a");
        rightArrowAnchor.setAttribute('id', 'rightarrowanchor');
        rightArrowAnchor.setAttribute('onclick', 'updatePostBlock(postL, '+(current_page+1)+');'); 
        rightArrowAnchor.innerHTML = ">>";
    
        var leftNumber = document.createElement("a");
        leftNumber.setAttribute('id', 'leftnumber');
        leftNumber.setAttribute('href', 'updatePostBlcok(postL, 1);');
        leftNumber.innerHTML = LEFT_NUM;
    
        var rightNumber = document.createElement("a");
        rightNumber.setAttribute('id', 'rightnumber');
        rightNumber.setAttribute('href', 'updatePostBlock(postL, '+RIGHT_NUM+');');
        rightNumber.innerHTML = RIGHT_NUM;
    
        var leftDotsSpan = document.createElement("span");
        leftDotsSpan.setAttribute('id', 'leftdotsspan');
        leftDotsSpan.innerHTML = "...";
    
        var rightDotsSpan = document.createElement("span");
        rightDotsSpan.setAttribute('id', 'rightdotsspan');
        rightDotsSpan.innerHTML = "...";
    
        var currentSpan = document.createElement("span"); //Span with current page number; corresponds to current page anchor above
        currentSpan.setAttribute('id', 'currentspan');
        currentSpan.innerHTML = current_page;
    
        /*
        * COMPONENT ATTACHMENT
        */ 
    
        indexBar.appendChild(leftArrowAnchor);
        indexBar.appendChild(leftNumber);
        indexBar.appendChild(leftDotsSpan);
    
        for (var i = (current_page - PROXIMATE); i < current_page; ++i) { //Put in left proximate anchors
            var proximate_anchor = document.createElement("a");
            proximate_anchor.setAttribute('class', 'leftproximate');
            proximate_anchor.setAttribute('onclick', 'updatePostBlock(postL, '+(current_page-i)+');');
            proximate_anchor.innerHTML = i;
            indexBar.appendChild(proximate_anchor);
        }
    
        indexBar.appendChild(currentSpan);
    
        for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) { //Put in right proximate anchors
            var proximate_anchor = document.createElement("a");
            proximate_anchor.setAttribute('class', 'rightproximate');
            proximate_anchor.setAttribute('onclick', 'updatePostBlock(postL, '+(current_page+i)+');');
            proximate_anchor.innerHTML = i;
            indexBar.appendChild(proximate_anchor);
        }
    
        indexBar.appendChild(rightDotsSpan);
        indexBar.appendChild(rightNumber);
        indexBar.appendChild(rightArrowAnchor);
    
        var leftProximateAnchors = document.querySelectorAll('a.leftproximate');
        var rightProximateAnchors = document.querySelectorAll('a.rightproximate');
        /*
        * COMPONENT HIDING/SHOWING (ALGORITHM)
        */
    
        if (RIGHT_NUM == 1) {
            leftArrowAnchor.style.display = "none";
            leftNumber.style.display = "none";
            leftDotsSpan.style.display = "none";
            currentSpan.style.display = "inline";
            rightDotsSpan.style.display = "none";
            rightNumber.style.display = "none";
            rightArrowAnchor.style.display = "none";
            var j = 0;
            for (var i = (current_page - PROXIMATE); i < current_page; ++i) {
            if (i <= LEFT_NUM) {
                leftProximateAnchors[j].style.display = "none";
            }
            ++j;
            }
            j = 0; //Used to keep track of the rightProximateAnchors array
            for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) {
            if (i >= RIGHT_NUM) {
                rightProximateAnchors[j].style.display = "none";
            }
            ++j;
            }
        }
        else {
            if ((current_page - PROXIMATE) <= (LEFT_NUM + 1)) {
            leftDotsSpan.style.display = "none";
            }
            if ((current_page + PROXIMATE) >= (RIGHT_NUM - 1)) {
            rightDotsSpan.style.display = "none";
            }
            if (current_page == LEFT_NUM) {
            leftArrowAnchor.style.display = "none";
            leftNumber.style.display = "none";
            }
            else if (current_page == RIGHT_NUM) {
            rightArrowAnchor.style.display = "none";
            rightNumber.style.display = "none";
            }
            var j = 0;
            for (var i = (current_page - PROXIMATE); i < current_page; ++i) {
            if (i <= LEFT_NUM) {
                leftProximateAnchors[j].style.display = "none";
            }
            ++j;
            }
            j = 0; //Used to keep track of the rightProximateAnchors array
            for (var i = (current_page + 1); i <= (current_page + PROXIMATE); ++i) {
            if (i >= RIGHT_NUM) {
                rightProximateAnchors[j].style.display = "none";
            }
            ++j;
            }
        }
    }

    function updatepostBlock(postL, pageNum) {
        for(let i = 0; i < 20; i++) {
            authorLink[i].setAttribute("href", "/profile?username="+postL.Posts[(pageNum-1)*20+i].Author);
            _postT[i].innerHTML = postL.Posts[(pageNum-1)*20+i].Title;
        }
    }

    xhttp.onreadystatechange = function() {
        if(this.readyState == 4 && this.status == 200) {
            postL = JSON.parse(this.responseText);
            createPostBlock(postL);
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