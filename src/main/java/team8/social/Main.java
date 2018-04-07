/*****
 * Object: Main
 * Description: 
 * Author: William Graham
 * Modified: April 04, 2018
 *****/
package team8.social;

import java.util.Random;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args){
        System.out.print("Social Server Started\n");
        
        configure();
        
        //define pages
        pages();
    }
    
    public static void configure(){
        port(80);
        Database.initialize("jdbc:mysql://localhost:3306/sys", "root", "password");
        staticFiles.location("/public");
        staticFiles.externalLocation("./resources");
    }
    
    /**
     * Defines how URIs are handled.
     * @post Server knows how to handle a number of URIs
     */
    public static void pages() {
        /*Handles the request to the root directory.*/
        get("/", (req, res) -> {
            if (!Session.validate(req.session().attribute("username"), req.session().attribute("sessionid"))){
                res.redirect("/login");
            } else {
                res.redirect("/home");
            }
    
            return null;
        });
    
        /*Handles the request to login*/
        get("/login", (req, res) -> {
            if (!Session.validate(req.session().attribute("username"), req.session().attribute("sessionid"))) {
                res.redirect("/html/login.html");
                return null;
            }
            
            res.redirect("/home");
            return null;
        });
        post("/login", (req, res) -> {
            if(Session.validate(req.session().attribute("username"), req.session().attribute("sessionid"))){
               res.redirect("/home");
               return null;
            }
            
            String userid = Account.login((String) req.queryParams("username"), (String) req.queryParams("password")).getUsername();
            Integer sessionid = (int) Math.random();
    
            if (userid != null) {
                req.session(true);
                req.session().attribute("UserID", userid);
                req.session().attribute("SessionID", sessionid);

                res.redirect("/home");
            } else {
                res.redirect("html/login.html?error=invalid");
            }
    
            return null;
        });
    
        //Create Account
        get("/createaccount", (req, res) -> {
            if(!Session.validate(req.session().attribute("username"), req.session().attribute("sessionid"))) {
                res.redirect("/html/createaccount.html");
                return null;
            }
            
            res.redirect("/home");
            return null;
        });
        post("/createaccount", (req, res) ->{
            if(Session.validate(req.session().attribute("username"), req.session().attribute("sessionid"))){
               res.redirect("/home");
               return null;
            }
            
            try{
            
            }catch(Exception e){
            
            }
            return null;
        });
    }
}
