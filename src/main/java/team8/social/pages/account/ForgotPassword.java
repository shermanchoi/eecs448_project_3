package team8.social.pages.account;

import team8.social.Account;
import team8.social.PageHandler;
import team8.social.Session;

import static spark.Spark.get;
import static spark.Spark.post;

public class ForgotPassword implements PageHandler {
    public void pages() {
        //Forgot Password
        get("/forgotpassword", (req,res)->{
            if(Session.validate(req.session().id(), req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
            
            if(req.session().attribute("forgot-username") == null){
                res.redirect("/html/forgotPasswordUsernameInput.html");
                return null;
            }
            
            return null;
        });
        
        post("/forgotpassword", (req, res)->{
            if(Session.validate(req.session().id(), req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
            
            if(req.session().attribute("forgot-username") == null){
                String user = req.queryParams("uname");
                if(Account.getSecurityQuestions(user) == null){
                    res.redirect("/forgotpassword");
                }else{
                    req.session().attribute("forgot-username", user);
                    res.redirect("/html/forgotPasswordSecQuestion.html");
                }
            }
            
            return null;
        });
    }
}
