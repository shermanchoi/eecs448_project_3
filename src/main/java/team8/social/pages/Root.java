package team8.social.pages;

import spark.Route;
import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;

public class Root implements PageHandler {
    private static String home = "";
    public Root(){
        try{
            home = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/main.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/main.html");
				home = new String(i.readAllBytes());
			} catch (Exception e2) {
				
			}
        }
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
