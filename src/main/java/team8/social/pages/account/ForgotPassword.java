package team8.social.pages.account;

import sun.nio.cs.StandardCharsets;
import team8.social.Account;
import team8.social.PageHandler;
import team8.social.Session;

import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;
import static spark.Spark.post;

public class ForgotPassword implements PageHandler {
    private static String username = "";
    private static String security = "";
    
    public ForgotPassword(){
        try {
            username = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/forgotPasswordUsernameInput.html").toURI())
            ));
        }catch(Exception e){
        
        }
        
        try{
            security = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/forgotPasswordSecQuestion.html").toURI())
            ));
        }catch(Exception e){
        
        }
    }
    
    public void pages() {
        //Forgot Password
        get("/forgotpassword", (req,res)->{
            if(Session.validate(req.session().id(), req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
            
            if(req.session().attribute("forgot-username") == null){
                //res.redirect("/html/forgotPasswordUsernameInput.html");
                return username;
            }else{
                //res.redirect("/html/forgotPasswordSecQuestion.html");
                return security;
            }
            
            //return null;
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
                    req.session().attribute("forgot-validated", "false");
                    
                    //res.redirect("/html/forgotPasswordSecQuestion.html");
                    return security;
                }
            }
            
            return null;
        });
    }
}
