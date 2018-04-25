/*****
 * Object: Main
 * Description: 
 * Author: William Graham
 * Modified: April 04, 2018
 *****/
package team8.social;


import team8.social.pages.*;
import team8.social.pages.account.*;
import team8.social.pages.post.*;

import java.util.Vector;

import static spark.Spark.*;


public class Main {
    public static void main(String[] args){    	
        System.out.print("Social Server Started\n");
        configure();
        //define pages
        pages();
        
        System.out.print("Social Server Configured\n");
    }
    
    public static void configure(){
        port(80);
        //("jdbc:mysql://localhost:3306/sys","root","password")
        Database.initialize("jdbc:mysql://mysql.eecs.ku.edu/w751g500", "w751g500", "eig4Jaix");
        staticFiles.location("/public");
        staticFiles.externalLocation("./resources");
    }
    
    /**
     * Defines how URIs are handled.
     * @post Server knows how to handle a number of URIs
     */
    public static void pages() {
        Vector<PageHandler> pages = new Vector<PageHandler>();
        
        pages.add(new Root());
        
        //All pages that pertain to accounts
        pages.add(new CreateAccount());
        pages.add(new ForgotPassword());
        pages.add(new Login());
        pages.add(new Logout());
        
        //All pages that pertain to posts
        pages.add(new ViewPost());
        pages.add(new CreatePost());
        
        //All pages that pertain to administration
        
        
        //Registers all of the pages
        for(Integer i = 0; i < pages.size(); i += 1){
            pages.get(i).pages();
        }
        
        get("/postViewReply", (req, res)->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
            
            res.redirect("/html/postViewReply.html?postID=" + Integer.parseInt(req.queryParams("postID")));
            
            return null;
        });
        
        post("/postViewReply", (req, res)->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
            
            Post p = Post.createPost(req.session().attribute("UserID"), req.queryParams("replycontent"), "Reply",Integer.parseInt(req.queryParams("postID")));
           
            if(p == null){
                res.redirect("/html/postViewReply.html?postID=" + Integer.parseInt(req.queryParams("postID")));
            }else{
                res.redirect("/html/postView.html?postID=" + Integer.parseInt(req.queryParams("postID")));
            }
            
            return null;
        });
            
        
        get("/api/posts", (req, res) ->{
            return Post.JSONAllPosts();
        });
        
        get("/api/post", (req,res)->{
           return Post.getPostByID(Integer.parseInt(req.queryParams("postID")));
        });
        
        get("/api/postReply", (req, res) ->{
            return Post.JSONAllPostReplies(Integer.parseInt(req.queryParams("postID")));
        });
        
        get("/api/security", (req,res)->{
           return Account.getSecurityQuestions(req.session().attribute("forgot-username"));
        });
    }
}
