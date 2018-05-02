package team8.social.pages;

import team8.social.Account;
import team8.social.Admin;
import team8.social.PageHandler;
import team8.social.Post;

import static spark.Spark.get;

/*****
 * Object: API
 * Description: 
 * Author: William Graham
 * Modified: May 01, 2018
 *****/

public class API implements PageHandler{
    
    public API(){
    
    }
    
    public void pages(){
        get("/api/posts", (req, res) -> {
            return Post.JSONAllPosts();
        });
    
        get("/api/post", (req, res) -> {
            return Post.getPostByID(Integer.parseInt(req.queryParams("postID")));
        });
    
        get("/api/postReply", (req, res) -> {
            return Post.JSONAllPostReplies(Integer.parseInt(req.queryParams("postID")));
        });
    
        get("/api/security", (req, res) -> {
            return Account.getSecurityQuestions(req.session().attribute("forgot-username"));
        });
        
        get("/api/users", (req, res) -> {
            return Admin.getAdminViewJSON();
        });
        
        get("/api/isAdmin", (req, res) -> {
            return Admin.isAdmin(req.session().attribute("username")) ? 0 : 1;
        });
        
        get("/api/profilePage", (req, res) -> {
            String user = req.session().attribute("UserID");
            String profile = req.queryParams("user");
            
            if(req.queryParams("user").toString() != "") {
                return Account.getProfilePageInformation(profile, user);
            }else{
                return Account.getProfilePageInformation(user, user);
            }
        });
    }
}
