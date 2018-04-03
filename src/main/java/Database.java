import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Database {
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  final static String url = "jdbc:mysql://localhost:3306/sys";  /**The url of the database, such as "jdbc:mysql://localhost:3306/sys"*/
  final static String user = "root";  /**The user of the database, such as "root"*/
  final static String pass = "password";  /**The password of the database's user; this will vary*/
  static Connection connection; /**The connection to the database that is created in method 'connect'*/
  /**
   * This method begins a connection with the database.
   * @pre None
   * @post The database is now connected to the database according to parameters.
   * @param url_in The URL of the database.
   * @param user_in The username used to log into the database.
   * @param pass_in The password used to log into the database.
   */
  public static void connect(){
    try {
      //Attempt to connect.
      System.out.println("Connecting...");
      connection = DriverManager.getConnection(url,user,pass);
      //Connection successful
      System.out.println("Connection Successful");
    }catch(Exception e){
      //Connection error
      System.out.println("Connection Error:\n\t" + e.getMessage());
    }
  }
  /**
   * This method executes a mySQL query and places new data.
   * @pre There is a connection to the database.
   * @post The database is altered depending on the query.
   * @param query The query as a string to be put into mySQL and executed.
   * @return true if the query executed without problems, false otherwise (boolean)
   */
  private static boolean querySQLSet(String query) {
    Database.connect();
    boolean successful = false;
	try{
	  System.out.println("Executing Statement:\n\t" + query);
	  Statement statement = connection.createStatement();
	  statement.executeUpdate(query);
	   
	  statement.close();
	  System.out.println("Execution Success");
	  
	  successful = true;
	}catch(Exception e){
	  System.out.println("Query Error:\n\t" + e.getMessage());
	}
	Database.disconnect();
	return successful;
  }
  /**
   * This is an account creation method.
   * @pre There is a connection to the database.
   * @post The account is created.
   * @param username The username of the new account.
   * @param password The password of the new account.
   * @return true if the account was created successfully, false otherwise (boolean)
   */
  public static boolean createAccount(String username, String password, String birthday, String firstName, String lastName){
	return querySQLSet("INSERT INTO Accounts (username,password,birthday,firstName,lastName) VALUE " + "(\"" + username + "\",\"" + password + "\",\"" + birthday + "\",\"" + firstName + "\",\"" + lastName + "\");");
  }
  /**
   * This is an post creation method.
   * @pre There is a connection to the database.
   * @post The post is created.
   * @param author The author of the message indicated by username.
   * @param message The message's contents.
   * @return true if the post was created successfully, false otherwise (boolean)
   */
  public static boolean createPost(String author, String message){
	return querySQLSet("INSERT INTO Posts (author,message) VALUE " + "(\"" + author + "\",\"" + message + "\");");
  }
  
  public static boolean login(String username, String password){
	  boolean successful = false;
	  
	  try {
		ResultSet results = null;
		Database.connect();
		
		String query = "SELECT * FROM Accounts WHERE username = \"" + username + "\";";
		
		try{
			System.out.println("Executing Statement:\n\t" + query);
			Statement statement = connection.createStatement();
			results = statement.executeQuery(query);
			
			while(results.next()) {
				successful = (password.equals(results.getString("password")));
			}
			
			results.close();
			statement.close();
			Database.disconnect();
			System.out.println("Execution Success");
		}catch(Exception e){
			System.out.println("Query Error:\n\t" + e.getMessage());
		}
	  }catch(Exception e){
		  System.out.println("Login Error:\n\t" + e.getMessage());
	  }
	  return successful;
  }
  
  public static void disconnect(){
    try{
      System.out.println("Connection closing...");
      connection.close();
      System.out.println("Connection closed.");
    }catch(Exception e){
      System.out.println("Connection Error:\n\t" + e.getMessage());
	}
  }
}