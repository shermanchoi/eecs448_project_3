package team8.social;


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
	private static String url = "jdbc:mysql://localhost:3306/sys";
	/**
	 * The user of the database, such as "root"
	 */
	private static String user = "root";
	/**
	 * The password of the database's user; this will vary
	 */
	private static String pass = "password";

	/**
	 * This method prepares the database for use.
	 * 
	 * @post Prepares the database for use.
	 * @param url_in
	 *            The url of the database
	 * @param user_in
	 *            The user of the database
	 * @param pass_in
	 *            The password of the user of the database
	 */
	public static void initialize(String url_in, String user_in, String pass_in) {
		url = url_in;
		user = user_in;
		pass = pass_in;
		
		boolean existenceAccounts = false;
		boolean existencePosts = false;
		boolean existenceSessions = false;
		
		//Check which tables exist
		try {
			Connection connection = Database.connect();
			ResultSet results = null;

			String query = "SHOW TABLES WHERE Tables_in_sys='Accounts' OR Tables_in_sys='Posts' OR Tables_in_sys='Sessions';";

			try {
				System.out.println("Executing Statement:\n\t" + query);
				Statement statement = connection.createStatement();
				results = statement.executeQuery(query);

				while (results.next()) {
					System.out.println(results.getString("Tables_in_sys"));
					switch(results.getString("Tables_in_sys")){
						case "Accounts":
							existenceAccounts = true;
							break;
						case "Posts":
							existencePosts = true;
							break;
						case "Sessions":
							existenceSessions = true;
							break;
					}
				}

				results.close();
				statement.close();
				Database.disconnect(connection);
				System.out.println("Execution Success");
			} catch (Exception e) {
				System.out.println("Query Error:\n\t" + e.getMessage());
			}
			
			
			//Generate Accounts table if it does not exist.
			if(!existenceAccounts) {
				Database.querySQLSet("CREATE TABLE `Posts` (\n" + 
						"  `author` varchar(255) NOT NULL,\n" + 
						"  `message` longtext NOT NULL,\n" + 
						"  `title` longtext NOT NULL,\n" + 
						"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" + 
						"  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
						"  PRIMARY KEY (`id`),\n" + 
						"  KEY `author` (`author`),\n" + 
						"  CONSTRAINT `Posts_ibfk_1` FOREIGN KEY (`author`) REFERENCES `Accounts` (`username`)\n" + 
						") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
			}
			//Generate Posts table if it does not exist.
			if(!existencePosts) {
				Database.querySQLSet("CREATE TABLE `Posts` (\n" + 
						"  `author` varchar(255) NOT NULL,\n" + 
						"  `message` longtext NOT NULL,\n" + 
						"  `title` longtext NOT NULL,\n" + 
						"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" + 
						"  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
						"  `parentPost` int(11) DEFAULT NULL,\n" + 
						"  PRIMARY KEY (`id`),\n" + 
						"  KEY `author` (`author`),\n" + 
						"  KEY `fk_Posts_1_idx` (`parentPost`),\n" + 
						"  CONSTRAINT `Posts_ibfk_1` FOREIGN KEY (`author`) REFERENCES `Accounts` (`username`),\n" + 
						"  CONSTRAINT `fk_Posts_1` FOREIGN KEY (`parentPost`) REFERENCES `Posts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" + 
						") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
			}
			//Generate Session table if it does not exist.
			if(!existenceSessions) {
				Database.querySQLSet("CREATE TABLE `Sessions` (\n" + 
						"  `sessionID` varchar(255) NOT NULL,\n" + 
						"  `username` varchar(255) NOT NULL,\n" + 
						"  PRIMARY KEY (`sessionID`),\n" + 
						"  KEY `username_idx` (`username`),\n" + 
						"  CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `Accounts` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" + 
						") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
			}
			
			//Delete session items. The entires are runtime stuff.
			Database.querySQLSet("TRUNCATE Sessions;");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

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
	 * This method executes a mySQL query and alters data.
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