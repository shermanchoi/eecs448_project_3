package team8.social.pages.post;

import team8.social.PageHandler;
import team8.social.Session;

import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;

public class ViewPost implements PageHandler {
    private static String view = "";
    private static String reply = "";
    
    public ViewPost(){
        try{
            view = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/postView.html").toURI())
            ));
        }catch(Exception e){}
    
        try{
            reply = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/postViewReply.html").toURI())
            ));
        }catch(Exception e){}
    }
    
    public void pages() {
        get("/post", (req, res)->{
            if (!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/login");
                return null;
            }
            
            return view;
        });
    }
}
