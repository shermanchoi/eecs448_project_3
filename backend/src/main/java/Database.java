import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Database {
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  private static String url;  /**The url of the database, such as "jdbc:mysql://localhost:3306/sys"*/
  private static String user;  /**The user of the database, such as "root"*/
  private static String pass;  /**The password of the database's user; this will vary*/
  private static Connection connection; /**The connection to the database that is created in method 'connect'*/
  
  /**
   * This method begins a connection with the database.
   * @pre None
   * @post The database is now connected to the database according to parameters.
   * @param url_in The URL of the database.
   * @param user_in The username used to log into the database.
   * @param pass_in The password used to log into the database.
   */
  public static void connect(String url_in, String user_in, String pass_in) {
	//Set static variables
    url = url_in;
    user = user_in;
    pass = pass_in;
    
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
   * This method executes a mySQL query and returns data.
   * @pre There is a connection to the database.
   * @param query The query as a string to be put into mySQL and executed.
   * @return The data returned from MySQL's query (ResultSet)
   */
  private static ResultSet querySQLGet(String query){
	ResultSet results = null;
    try{
	    System.out.println("Executing Statement:\n\t" + query);
	    Statement statement = connection.createStatement();
	    results = statement.executeQuery(query);
	    
	    statement.close();
	    System.out.println("Execution Success");
	}catch(Exception e){
		System.out.println("Query Error:\n\t" + e.getMessage());
	}
	return results;
  }
  /**
   * This method executes a mySQL query and places new data.
   * @pre There is a connection to the database.
   * @post The database is altered depending on the query.
   * @param query The query as a string to be put into mySQL and executed.
   * @return true if the query executed without problems, false otherwise (boolean)
   */
  private static boolean querySQLSet(String query) {
	try{
	  System.out.println("Executing Statement:\n\t" + query);
	  Statement statement = connection.createStatement();
	  statement.executeUpdate(query);
	   
	  statement.close();
	  System.out.println("Execution Success");
	  return true;
	}catch(Exception e){
	  System.out.println("Query Error:\n\t" + e.getMessage());
	  return false;
	}
  }
  /**
   * This is an account creation method.
   * @pre There is a connection to the database.
   * @post The account is created.
   * @param username The username of the new account.
   * @param password The password of the new account.
   * @return true if the account was created successfully, false otherwise (boolean)
   */
  public static boolean createAccount(String username, String password){
	return querySQLSet("INSERT INTO Accounts (username,password) VALUE " + "(\"" + username + "\",\"" + password + "\");");
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