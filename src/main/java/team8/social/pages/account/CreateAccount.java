package team8.social.pages.account;

import team8.social.Account;
import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.post;

public class CreateAccount implements PageHandler{
    private static String create = "";
    
    public CreateAccount(){
        try{
            create = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/createAccount.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/createAccount.html");
        		create = new String(IOUtils.toByteArray(i));
			} catch (Exception e2) {
				
			}
        }
    }
    
    public void pages(){
        //Create Account
        get("/createaccount", (req, res) -> {
            //If there is NOT a logged in session. Go as intended
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))) {
                return create;
            }
        
            //Go to home otherwise
            res.redirect("/home");
            return null;
        });
    
        /**
         * Create account action
         */
        post("/createaccount", (req, res) ->{
            //Logged in session already exists.
            if(Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/home");
                return null;
            }
        
            try {
                //Parameters of the account creation.
                String uname = req.queryParams("uname");
                String pword = req.queryParams("pword");
                String dateOfBirth = req.queryParams("Year") + "-" + req.queryParams("Month") + "-" + req.queryParams("Date");
                String fName = req.queryParams("fname");
                String lName = req.queryParams("lname");
                String secQ1 = req.queryParams("sq1");
                String secQ2 = req.queryParams("sq2");
                String secQ3 = req.queryParams("sq3");
                String ansQ1 = req.queryParams("sa1");
                String ansQ2 = req.queryParams("sa2");
                String ansQ3 = req.queryParams("sa3");
            
                if(pword.length() < 8) {
                    //The password was not long enough.
                    res.redirect("/createaccount");
                }
            
                //Attempt account creation
                Account a = Account.createAccount(uname, pword, dateOfBirth, fName, lName, secQ1, secQ2, secQ3, ansQ1, ansQ2, ansQ3);
            
                if(a != null) {
                    //The account is now created. Login as that account
                    req.session(true);
                    req.session().attribute("UserID", uname);
                    Session.createSession(req.session().id(),req.session().attribute("UserID"));
                
                    //Go to '/home' since the account is created
                    res.redirect("/home");
                }
            }finally{
                //Something did not happen correctly
                res.redirect("/createaccount");
            }
        
        
            return null;
        });
    }
}
