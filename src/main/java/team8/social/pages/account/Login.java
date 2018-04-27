package team8.social.pages.account;

import team8.social.Account;
import team8.social.PageHandler;
import team8.social.Session;

import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;
import static spark.Spark.post;

/*****
 * Object: Login
 * Description: 
 * Author: William Graham
 * Modified: April 18, 2018
 *****/

public class Login implements PageHandler{
    private static String source = "";
    
    public Login(){
        try{
            source = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/login.html").toURI())
            ));
        }catch (Exception e){}
    }
    public void pages(){
        /*Handles the request to login*/
        get("/login", (req, res) -> {
            //If there is NOT a logged in session. Redirect to login
            if (!Session.validate(req.session().id(),req.session().attribute("UserID"))) {
                return source;
            }
            //Otherwise, go to home
            res.redirect("/home");
            return null;
        });
        /**
         * Login Attempt
         */
        post("/login", (req, res) -> {
            //If there is already a logged in session. Redirect to home
            if(Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
        
            //Attempt to log in using username and password
            try {
                Account user = Account.login(req.queryParams("username"), req.queryParams("password"));
                if (user != null) {
                    //Successful login, create session.
                    req.session(true);
                    req.session().attribute("UserID", user.getUsername());
                    Session.createSession(req.session().id(),req.session().attribute("UserID"));
                
                    res.redirect("/home");
                }else {
                    //Account does not exist.
                    return source;
                }
            }catch(Exception e) {
                //Invalid input.
                return source;
            }
        
            return null;
        });
    }
}
