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
            if (!Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))){
                res.redirect("/login");
            } else {
                res.redirect("/home");
            }
    
            return null;
        });
    
        /*Handles the request to login*/
        get("/login", (req, res) -> {
            if (!Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))) {
                res.redirect("/html/login.html");
                return null;
            }
            
            res.redirect("/home");
            return null;
        });
        post("/login", (req, res) -> {
            if(Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))){
            	
               res.redirect("/home");
               return null;
            }
            try {
	            String userid = Account.login((String) req.queryParams("username"), (String) req.queryParams("password")).getUsername();
	            Integer sessionid = (int) Math.random() * 100000000;
	    
	            if (userid != null) {
	                req.session(true);
	                req.session().attribute("UserID", userid);
	                req.session().attribute("SessionID", sessionid);
	                Session.createSession(sessionid.toString(), userid);
	                
	                res.redirect("/home");
	            } else {
	                res.redirect("html/login.html?error=invalid");
	            }
            }catch(Exception e) {
            	
            }
            return null;
        });
    
        //Create Account
        get("/createaccount", (req, res) -> {
            if(!Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))) {
                res.redirect("/html/createaccount.html");
                return null;
            }
            
            res.redirect("/home");
            return null;
        });
        post("/createaccount", (req, res) ->{
            if(Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))){
               res.redirect("/home");
               return null;
            }
            
            try {
            	//Parameters of the post
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
					//The password was not long enough
					res.redirect("/createaccount");
				}
				
				Account a = Account.createAccount(uname, pword, dateOfBirth, fName, lName, secQ1, secQ2, secQ3, ansQ1, ansQ2, ansQ3);
				
			
				if(a != null) {
					//Create the session since the account is created.
					String userid = Account.login(uname, pword).getUsername();
		            Integer sessionid = (int) Math.random() * 100000000;
		            
		            req.session(true);
	                req.session().attribute("UserID", userid);
	                req.session().attribute("SessionID", sessionid);
	                Session.createSession(sessionid.toString(), userid);
	                
	                //Go to '/home' since the account is created
	                res.redirect("/home");
				}
			} catch (Exception e) {

			}
            
            //Something did not happen correctly
            res.redirect("/html/createaccount.html");
            return null;
        });
        
        //Main page if the user is logged in.
        get("/home", (req, res) ->{
            if(Session.validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))) {
        		res.redirect("/html/main.html");
            }
        	
        	return null;
            });
    }
}
