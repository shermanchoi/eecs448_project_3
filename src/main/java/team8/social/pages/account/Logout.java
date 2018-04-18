package team8.social.pages.account;

import team8.social.PageHandler;
import team8.social.Session;

import static spark.Spark.get;

/*****
 * Object: Logout
 * Description: 
 * Author: William Graham
 * Modified: April 18, 2018
 *****/

public class Logout implements PageHandler {
    public void pages(){
        get("/logout", (req,res)->{
            if(!Session.validate(req.session().id(), req.session().attribute("UserID"))){
                res.redirect("/login");
                return null;
            }
        
            Session.deleteSession(req.session().id(), req.session().attribute("UserID"));
            req.session().removeAttribute("UserID");
            req.session(false);
            res.redirect("/login");
        
            return null;
        });
    }
}
