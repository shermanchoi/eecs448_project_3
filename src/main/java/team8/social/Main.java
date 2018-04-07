/*****
 * Object: Main
 * Description: 
 * Author: William Graham
 * Modified: April 04, 2018
 *****/
package team8.social;


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
        Database.initialize("jdbc:mysql://mysql.eecs.ku.edu/w751g500", "w751g500", "eig4Jaix");
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
            if (!Session.validate(req.session().id(),req.session().attribute("UserID"))){
                res.redirect("/login");
            } else {
                res.redirect("/home");
            }
    
            return null;
        });
    
        /*Handles the request to login*/
        get("/login", (req, res) -> {
        	//If there is NOT a logged in session. Redirect to login
            if (!Session.validate(req.session().id(),req.session().attribute("UserID"))) {
                res.redirect("/html/login.html");
                return null;
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
	            	res.redirect("html/login.html?error=invalid");
	            }
            }catch(Exception e) {
            	//Invalid input.
            	res.redirect("html/login.html?error=invalid");
            }
            
            return null;
        });
    
        //Create Account
        get("/createaccount", (req, res) -> {
        	//If there is NOT a logged in session. Go as intended
            if(!Session.validate(req.session().id(),req.session().attribute("UserID"))) {
                res.redirect("/html/createaccount.html");
                return null;
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
	            res.redirect("/html/createaccount.html");
			}
            
            
            return null;
        });
        
        //Main page if the user is logged in.
        get("/home", (req, res) ->{
        	//You must be logged in to get into the main page.
            if(Session.validate(req.session().id(),req.session().attribute("UserID"))) {
        		res.redirect("/html/main.html");
            }
        	
            //Go back to root otherwise.
            res.redirect("/");
            
        	return null;
        });
        
        get("/api/posts", (req, res) ->{
            String postList = "{\"Posts\": [{\"ID\": 1, \"Title\": \"Hello world!\", \"Author\": \"Death\", \"Reply\":0}]}";
            
            return postList;
        });
    }
}
