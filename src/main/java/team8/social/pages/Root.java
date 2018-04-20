package team8.social.pages;

import spark.Route;
import team8.social.PageHandler;
import team8.social.Session;

import static spark.Spark.get;

public class Root implements PageHandler {
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
                res.redirect("/html/main.html");
            }
        
            //Go back to root otherwise.
            res.redirect("/");
        
            return null;
        });
    }
}
