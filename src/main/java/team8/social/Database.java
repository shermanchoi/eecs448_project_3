import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	/**
	 * This is the driver used to connect to the MySQL server
	 */
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * The url of the database, such as "jdbc:mysql://localhost:3306/sys"
	 */
	final static String url = "jdbc:mysql://localhost:3306/sys";
	/**
	 * The user of the database, such as "root"
	 */
	final static String user = "root";
	/**
	 * The password of the database's user; this will vary
	 */
	final static String pass = "password";

	/**
	 * This method begins a connection with the database.
	 * 
	 * @pre None
	 * @post The database is now connected to the database according to parameters.
	 * @param url_in
	 *            The URL of the database.
	 * @param user_in
	 *            The username used to log into the database.
	 * @param pass_in
	 *            The password used to log into the database.
	 */
	public static Connection connect() {
		try {
			// Attempt to connect.
			System.out.println("Connecting...");
			Connection connection = DriverManager.getConnection(url, user, pass);
			// Connection successful
			System.out.println("Connection Successful");
			return connection;
		} catch (Exception e) {
			// Connection error
			System.out.println("Connection Error:\n\t" + e.getMessage());
			return null;
		}
	}

	/**
	 * This method closes a database connection.
	 * 
	 * @pre The connection parameter is a valid, open connection
	 * @post The connection will be closed
	 * @param connection
	 *            The connection to be closed
	 */
	public static void disconnect(Connection connection) {
		try {
			System.out.println("Connection closing...");
			connection.close();
			System.out.println("Connection closed.");
		} catch (Exception e) {
			System.out.println("Connection Error:\n\t" + e.getMessage());
		}
	}

	/**
	 * This method executes a mySQL query and places new data.
	 * 
	 * @pre There is a connection to the database.
	 * @post The database is altered depending on the query.
	 * @param query
	 *            The query as a string to be put into mySQL and executed.
	 * @return true if the query executed without problems, false otherwise
	 *         (boolean)
	 */
	public static boolean querySQLSet(String query) {
		Connection connection = Database.connect();
		boolean successful = false;
		try {
			System.out.println("Executing Statement:\n\t" + query);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			statement.close();
			System.out.println("Execution Success");

			successful = true;
		} catch (Exception e) {
			System.out.println("Query Error:\n\t" + e.getMessage());
		}
		Database.disconnect(connection);
		return successful;
	}

}