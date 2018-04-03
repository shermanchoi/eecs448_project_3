import static spark.Spark.*;

import spark.Session;

public class Main {
  public static void main(String[] args) {
   port(4567);
   
   staticFiles.location("/public");
   init();
   
   get("/",(req,res)-> {
	   res.redirect("/html/login.html");
	   
	   System.out.println((String)req.session().attribute("username"));
	   
	   return "";});
   post("/createAccount",(req,res)->{
	   String username = req.queryParams("uname");
	   String password = req.queryParams("pword");
	   String dateOfBirth = "1998-12-25";
	   String firstName = req.queryParams("fname");
	   String lastName = req.queryParams("lname");
	   
	   if(Database.createAccount(username, password, dateOfBirth, firstName, lastName)){
	      res.redirect("/html/login.html");
	   }else {
		  res.redirect("/html/createAccount.html");
	   }
	   Database.disconnect();
	   
	   
	   return "";
       });
   post("/login",(req,res)->{
	   String uname = req.queryParams("username");
	   String pword = req.queryParams("password");
       if(Database.login(uname,pword)) {
    	 req.session(true);
	     req.session().attribute("username",uname);
	     
	     
    	 return "Login Successful!</br><a href=\"/\">Go Back</a>";
       }else {
    	 res.redirect("/html/login.html");
    	 return "";
       }
       
       });
   
   /*
   get("/", (req,res) -> {
	 req.session(true);
  	 
     return 
     "  <form action=\"/\" method=\"post\">\n" + 
   	 "    <p>Please type in your Username:</p>\n" + 
   	 "    <input type=\"text\" name=\"username\" required></input>\n" + 
   	 "    <p>Please type in your Password:</p>\n" +
   	 "    <input type=\"text\" name=\"password\" required></input>\n" + 
   	 "    <p>A valid account has a password and username.</p>\n" + 
   	 "    <input type=\"submit\" value=\"Create Account\">\n" + 
   	 "  </form>" +
   	 "  <form action=\"/createPost\" method=\"post\">\n" + 
  	 "    <p>Please type in your Username:</p>\n" + 
  	 "    <input type=\"text\" name=\"username\" required></input>\n" + 
  	 "    <p>Please type in your Post:</p>\n" +
  	 "    <textarea name=\"message\" required></textarea>\n" + 
  	 "    <p>A post is from an existing user and has a non-empty message.</p>\n" + 
  	 "    <input type=\"submit\" value=\"Create Post\">\n" + 
  	 "  </form>" +
  	 "  <br><a href=\"./Users\">Go Back</a>";
     });
   
   post("/", (req,res) -> {
	   Database.connect("jdbc:mysql://localhost:3306/sys","root","password");
	   String username = req.queryParams("username");
	   String password = req.queryParams("password");
	   
       if(Database.createAccount(username,password)) {
    	 Database.disconnect();
         return "Account creation successful!" +
     	 		"<br><a href=\"./\">Go Back</a>";
       }else{
    	 Database.disconnect();
    	 return "Account creation failed!" +
     	 		"<br><a href=\"./\">Go Back</a>";
       }
   });
   post("/createPost", (req,res) -> {
	   Database.connect("jdbc:mysql://localhost:3306/sys","root","password");
	   String username = req.queryParams("username");
	   String message = req.queryParams("message");
	   
	   req.session().attribute("user",username);
	   
       if(Database.createPost(username,message)) {
    	 Database.disconnect();
         return "Post created!" +
     	 		"<br><a href=\"./\">Go Back</a>";
       }else{
    	 Database.disconnect();
    	 return "Post failed!" +
    	 		"<br><a href=\"./\">Go Back</a>";
       }
   });
   */
   
  }
}