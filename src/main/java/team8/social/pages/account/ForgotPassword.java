package team8.social.pages.account;

import team8.social.PageHandler;
import team8.social.Session;

import static spark.Spark.get;

/*****
 * Object: ForgotPassword
 * Description: 
 * Author: William Graham
 * Modified: April 18, 2018
 *****/

public class ForgotPassword implements PageHandler {
    public void pages() {
        //Forgot Password
        get("/forgotpassword", (req,res)->{
            if(Session.validate(req.session().id(), req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
        
            res.redirect("/html/forgotPasswordUsernameInput.html");
            return null;
        });
    }
}
