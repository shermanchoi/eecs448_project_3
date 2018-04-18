package team8.social.pages.post;

import team8.social.PageHandler;
import team8.social.Post;
import team8.social.Session;

import static spark.Spark.get;
import static spark.Spark.post;

public class CreatePost implements PageHandler{
    public void pages() {
        get("/createpost", (req,res) ->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
        
            res.redirect("/html/createPost.html");
            return null;
        });
    
    
        post("/createpost", (req, res)->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
        
            Post p = Post.createPost(req.session().attribute("UserID"), req.queryParams("postcontent"), req.queryParams("posttitle"));
            if(p == null){
                res.redirect("/createpost");
            }else{
                res.redirect("/home");
            }
        
            return null;
        });
    }
}
