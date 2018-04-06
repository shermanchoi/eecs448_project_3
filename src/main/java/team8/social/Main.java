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
    
    public static void pages(){
        /*Handles the request to the root directory.*/
        get("/", (req, res) -> {
            if(req.session().attribute("SessionID") == null){
                res.redirect("/login");
            }else{
                res.redirect("/home");
            }
            
            return null;
        });
        
        /*Handles the request to login*/
        get("/login", (req, res) ->{
            if(!validate(req.session().attribute("UserID"), req.session().attribute("SessionID"))) {
                res.redirect("/html/login.html");
            }else{
                //send to home
            }
            return null;
        });
        post("/login", (req, res)->{
            String userid = login(req.queryParams("username"), req.queryParams("password"));
            Integer sessionid = (int) Math.random();
            
            if(userid != null){
                req.session(true);
                req.session().attribute("UserID", userid);
                req.session().attribute("SessionID", sessionid);
                
                
                
                return "Successfully Logged In";
            }else{
                res.redirect("html/login.html?error=invalid");
            }
            
            return null;
        });
        
        //Create Account
        get("/createaccount", (req, res) -> {res.redirect("/createaccount.html"); return null;});
    }
    
    
    /**Logs the user in an creates a session.**/
    public static String login(String username, String password){
        try{
            //Check database set id if success
        	Account a = Account.login(username, password);
        	System.out.println(a.getUsername());
        	return a.getUsername();
        }catch(Exception e) {
        	
        }
        
        return null;
    }
    
    /**Validates a session. (User is logged in)**/
    public static boolean validate(String userid, String sessionid){
        
        return false;
    }
}
