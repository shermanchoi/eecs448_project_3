package team8.social.pages;

import spark.Route;
import spark.utils.IOUtils;
import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;

public class Root implements PageHandler {
    private static String home = "";
    public Root(){
        try{
            InputStream i = getClass().getResourceAsStream("/public/html/main.html");
            home = new String(IOUtils.toByteArray(i));
        }catch(Exception e){}
    }
    
    public void pages(){
        get("/", (req, res) -> {
            if (!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
            } else {
                res.redirect("/home");
            }
        
            return null;
        });
    
        get("/home", (req, res) ->{
            //You must be logged in to get into the main page.
            if(Session.validate(req.session().id(),req.session().attribute("UserID"))) {
                return home;
            }
        
            //Go back to root otherwise.
            res.redirect("/login");
        
            return null;
        });
    }
}
