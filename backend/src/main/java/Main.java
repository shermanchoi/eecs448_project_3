import static spark.Spark.*;

public class Main {
  public static void main(String[] args) {
   
   
   
   port(4567);
   
   get("/", (req,res) -> 
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
  	 "  </form>");
   
   post("/", (req,res) -> {
	   Database.connect("jdbc:mysql://localhost:3306/sys","root","password");
	   String username = req.queryParams("username");
	   String password = req.queryParams("password");
	   
       if(Database.createAccount(username,password)) {
    	 Database.disconnect();
         return "Account creation successful!";
       }else{
    	 Database.disconnect();
    	 return "Account creation failed!";
       }
   });
   post("/createPost", (req,res) -> {
	   Database.connect("jdbc:mysql://localhost:3306/sys","root","password");
	   String username = req.queryParams("username");
	   String message = req.queryParams("message");
	   
       if(Database.createPost(username,message)) {
    	 Database.disconnect();
         return "Post created!";
       }else{
    	 Database.disconnect();
    	 return "Post failed!";
       }
       
   });
   
   
  }
}