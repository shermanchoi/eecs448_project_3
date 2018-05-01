package team8.social.pages.post;

import team8.social.PageHandler;
import team8.social.Post;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.post;

public class CreatePost implements PageHandler{
    private static String create = "";
    
    public CreatePost(){
        try{
            create = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/createPost.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/createPost.html");
        		create = new String(IOUtils.toByteArray(i));
			} catch (Exception e2) {
				
			}
        	
        }
    }
    
    public void pages() {
        get("/createpost", (req,res) ->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
            
            return create;
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
