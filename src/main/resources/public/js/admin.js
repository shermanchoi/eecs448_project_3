let error = parent.document.URL.substring(parent.document.URL.indexOf('?banError='), parent.document.URL.length);
error = error.slice(10);

if(error == "yourself"){
	alert("You should not and cannot ban yourself.");
}else if(error == "notFound"){
	alert("The user you attempted to ban could not be found.");
}

function populatePosts(text){
	let postL = JSON.parse(text);
    let postsTable = document.getElementById("postsTable");
    
    for(let i = 0; i < postL.Posts.length; i++) {
        
        let post = document.createElement("tr");
        let title = document.createElement("td");
        let author = document.createElement("td");
        let repliesNum = document.createElement("td");
        
        let deleteButtonCol = document.createElement("td");
        let deleteButtonForm = document.createElement("form");
        let deleteButton = document.createElement("button");
        
        title.setAttribute("class", "postTitle");
        author.setAttribute("class", "postAuthor");
        repliesNum.setAttribute("class", "replyNbr");
        
        deleteButtonForm.setAttribute("action","/admin/deletePost");
        deleteButtonForm.setAttribute("method","post");
        deleteButton.setAttribute("type","submit");
        deleteButton.setAttribute("name","ID");
        deleteButton.setAttribute("value",postL.Posts[i].ID);
        deleteButton.innerHTML = "Delete Post";
        
        title.innerHTML = postL.Posts[i].Title;
        author.innerHTML = postL.Posts[i].Author;
        repliesNum.innerHTML = postL.Posts[i].Reply;
        
        post.appendChild(title);
        post.appendChild(author);
        post.appendChild(repliesNum);

        deleteButtonForm.appendChild(deleteButton);
        deleteButtonCol.appendChild(deleteButtonForm);
        post.appendChild(deleteButtonCol);
        
        postsTable.appendChild(post);        
    }
}

function populateUsers(text){
	let list = JSON.parse(text);
    let userTable = document.getElementById("userTable");
    
    for(let i = 0; i < list.Users.length; i++) {

        let user = document.createElement("tr");
        let username = document.createElement("td");
        let href = document.createElement("a");
        let text = list["Users"][i].username;
        href.innerHTML = text;
        
        let banButtonCol = document.createElement("td");
        let banButtonForm = document.createElement("form");
        let banButton = document.createElement("button");
        
        banButtonForm.setAttribute("method","post");
        banButton.setAttribute("type","submit");
        banButton.setAttribute("name","username");
        banButton.setAttribute("value",list["Users"][i].username);
        
        if(list["Users"][i].banned == 0){
	        banButtonForm.setAttribute("action","/admin/banUser");
	        banButton.innerHTML = "Ban User";
        }else{
    	    banButtonForm.setAttribute("action","/admin/unbanUser");
	        banButton.innerHTML = "Unban User";
        }
        
        
        //Change password functionality?
        /**
        let changePasswordCol = document.createElement("td");
        let changePasswordButtonForm = document.createElement("form");
        let changePasswordButton = document.createElement("button");
        let changePasswordText = document.createElement("input");
        
        changePasswordButtonForm.setAttribute("method","post");
        changePasswordButton.setAttribute("type","submit");
        changePasswordButton.setAttribute("name","username");
        changePasswordButton.setAttribute("value",list["Users"][i].username);
        changePasswordText.setAttribute("type","text");
        changePasswordText.setAttribute("name","password");
        changePasswordButton.setAttribute("action","/admin/changePassword");
        changePasswordButton.innerHTML = "Change";
        **/
        
        username.appendChild(href);
        user.appendChild(username);
        userTable.appendChild(user);
        
        banButtonForm.appendChild(banButton);
        banButtonCol.appendChild(banButtonForm);
        userTable.appendChild(banButtonCol);
    }
}

function createList () {
	let xhttp1 = new XMLHttpRequest();
    xhttp1.onreadystatechange = function() {
        if(this.readyState == 4 && this.status == 200) {
            populatePosts(this.responseText);
        }
    };


    xhttp1.open("GET", "/api/posts", true);
    xhttp1.send();
    

	let xhttp2 = new XMLHttpRequest(); 
	xhttp2.onreadystatechange = function() {
        if(this.readyState == 4 && this.status == 200) {
        	
        	populateUsers(this.responseText);
        }
    };

    xhttp2.open("GET", "/api/users", true);
    xhttp2.send();
}

createList();