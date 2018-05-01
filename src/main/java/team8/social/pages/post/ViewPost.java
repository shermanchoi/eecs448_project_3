package team8.social.pages.post;

import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.utils.IOUtils;

import static spark.Spark.get;

public class ViewPost implements PageHandler {
    private static String view = "";
    
    public ViewPost(){
        try{
            InputStream i = getClass().getResourceAsStream("/public/html/postView.html");
            view = new String(IOUtils.toByteArray(i));
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
