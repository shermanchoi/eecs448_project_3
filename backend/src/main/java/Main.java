import static spark.Spark.*;

public class Main {
  public static void main(String[] args) {
   Database.connect("jdbc:mysql://localhost:3306/sys","root","password");
   
   
   port(4567);
   
   get("/", (req,res) -> 
     "  <form action=\"/\" method=\"post\">\n" + 
   	 "    <p>Please type in your Username:</p>\n" + 
   	 "    <input type=\"text\" name=\"username\" required></input>\n" + 
   	 "    <p>Please type in your Password:</p>\n" +
   	 "    <input type=\"text\" name=\"password\" required></textarea>\n" + 
   	 "    <p>A valid account has a password and username.</p>\n" + 
   	 "    <input type=\"submit\" value=\"Create Post\">\n" + 
   	 "</form>");
   post("/", (req,res) -> {
	   String username = req.queryParams("username");
	   String password = req.queryParams("password");
	   
       if(Database.createAccount(username,password)) {
         return "Account created!";
       }else{
    	 return "Account creation failed!";
       }
   });
   
   
   
  }
}