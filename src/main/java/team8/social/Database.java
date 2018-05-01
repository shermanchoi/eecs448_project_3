package team8.social;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	public static boolean initialize(String url_in, String user_in, String pass_in) {
		url = url_in;
		user = user_in;
		pass = pass_in;
		
		boolean isSuccessful = true;
		// Check which tables exist
		try {
			// Generate tables if they do not exist.
			isSuccessful = isSuccessful && Database.querySQLSet(
					"CREATE TABLE IF NOT EXISTS `social_accounts` (" + 
					"  `username` varchar(255) NOT NULL," + 
					"  `password` varchar(255) NOT NULL," + 
					"  `birthday` date NOT NULL," + 
					"  `firstName` varchar(255) NOT NULL," + 
					"  `lastName` varchar(255) NOT NULL," + 
					"  `securityQuestion1` longtext NOT NULL," + 
					"  `securityQuestion2` longtext NOT NULL," + 
					"  `securityQuestion3` longtext NOT NULL," + 
					"  `securityAnswer1` longtext NOT NULL," + 
					"  `securityAnswer2` longtext NOT NULL," + 
					"  `securityAnswer3` longtext NOT NULL," + 
					"  `adminStatus` int(11) DEFAULT '0'," + 
					"  `banned` tinyint(1) DEFAULT '0'," + 
					"  `biography` longtext," + 
					"  `creationDate` date NOT NULL," + 
					"  PRIMARY KEY (`username`)" + 
					") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
					
					isSuccessful = isSuccessful && Database.querySQLSet("CREATE TABLE IF NOT EXISTS `social_posts` (  `author` varchar(255) NOT NULL,"
					+ "  `message` longtext NOT NULL,  `title` longtext,"
					+ "  `id` int(11) NOT NULL AUTO_INCREMENT,  `dateCreated` datetime DEFAULT NULL,"
					+ "  `parentPost` int(11) DEFAULT NULL,  PRIMARY KEY (`id`),"
					+ "  KEY `author` (`author`),  KEY `fk_Posts_1_idx` (`parentPost`),"
					+ "  CONSTRAINT `Posts_ibfk_1` FOREIGN KEY (`author`) REFERENCES `social_accounts` (`username`),"
					+ "  CONSTRAINT `fk_Posts_1` FOREIGN KEY (`parentPost`) REFERENCES `social_posts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

			isSuccessful = isSuccessful && Database.querySQLSet("CREATE TABLE IF NOT EXISTS `social_sessions` ("
					+ "  `sessionID` varchar(255) NOT NULL,  `username` varchar(255) NOT NULL,"
					+ "  PRIMARY KEY (`sessionID`),  KEY `username_idx` (`username`),"
					+ "  CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `social_accounts` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			return isSuccessful;
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return false;
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
			// System.out.println("Connecting...");
			Connection connection = DriverManager.getConnection(url, user, pass);
			// Connection successful
			// System.out.println("Connection Successful");
			return connection;
		} catch (Exception e) {
			// Connection error
			// System.out.println("Connection Error:\n\t" + e.getMessage());
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
			// System.out.println("Connection closing...");
			connection.close();
			// System.out.println("Connection closed.");
		} catch (Exception e) {
			// System.out.println("Connection Error:\n\t" + e.getMessage());
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
		try {
			// System.out.println("Executing Statement:\n\t" + query);
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);

			statement.close();
			// System.out.println("Execution Success");
			Database.disconnect(connection);
			
			return true;
		} catch (Exception e) {
			System.out.println("Query Error:\n\t" + e.getMessage());
			return false;
		}
	}

	/**
	 * This method drops all the tables in MySQL and reinitializes them.
	 * 
	 * @post Deletes all tables and recreates them based on initialization
	 *       variables. They will be empty.
	 */
	public static void hardReset() {
		DatabaseSetter setter = new DatabaseSetter(
				"DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;");
		try {
			// Execution of statement.
			setter.execute();
			Database.initialize(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * This class deals with getting information from the database.
 * 
 * @author Sherman Choi
 *
 */
class DatabaseGetter {
	/**
	 * The connection to the database.
	 */
	private Connection connection;
	/**
	 * The statement to be executed. It must be prepared prior to execution.
	 */
	public PreparedStatement statement = null;
	/**
	 * The result set that is returned as a result of execution.
	 */
	public ResultSet results = null;

	public DatabaseGetter(String query) {
		try {
			connection = Database.connect();
			try {
				// System.out.println("Preparing Statement:\n\t" + query);
				statement = connection.prepareStatement(query);
			} catch (Exception e) {
				// System.out.println("Query Preparation Error:\n\t" + e.getMessage());
			}
		} catch (Exception e) {
			// System.out.println("Connection Error:\n\t" + e.getMessage());
		}
	}

	public void execute() {
		try {
			results = statement.executeQuery();
		} catch (Exception e) {
			// System.out.println("Statement Execution Error:\n\t" + e.getMessage());
		}
	}

	public void finalize() {
		try {
			results.close();
			statement.close();
			Database.disconnect(connection);
		} catch (Exception e) {
			// System.out.println("Connection Closing Error:\n\t" + e.getMessage());
		}
	}
}

/**
 * This class deals with putting information into the database.
 * 
 * @author Sherman Choi
 */
class DatabaseSetter {
	/**
	 * The connection to the database.
	 */
	private Connection connection;
	/**
	 * The statement to be executed. It must be prepared prior to execution.
	 */
	public PreparedStatement statement = null;

	/**
	 * The constructor to the database setter
	 * 
	 * @param query
	 *            The query that represents a statement to be prepared.
	 */
	public DatabaseSetter(String query) {
		try {
			connection = Database.connect();
			try {
				// System.out.println("Preparing Statement:\n\t" + query);
				statement = connection.prepareStatement(query);
			} catch (Exception e) {
				// System.out.println("Query Preparation Error:\n\t" + e.getMessage());
			}
		} catch (Exception e) {
			// System.out.println("Connection Error:\n\t" + e.getMessage());
		}
	}

	/**
	 * This executes the statement.
	 * 
	 * @pre The statement has been prepared.
	 * @return True if the execution occurred without error, false otherwise.
	 */
	public boolean execute() {
		try {
			statement.executeUpdate();
			return true;
		} catch (Exception e) {
			// System.out.println("Statement Execution Error:\n\t" + e.getMessage());
			return false;
		}
	}

	/**
	 * The finalize overrider.
	 * 
	 * @post The connection and statement is closed.
	 */
	public void finalize() {
		try {
			statement.close();
		} catch (Exception e) {
			// System.out.println("Statement Closing Error:\n\t" + e.getMessage());
		}
		try {
			Database.disconnect(connection);
		} catch (Exception e) {
			// System.out.println("Connection Closing Error:\n\t" + e.getMessage());
		}
	}
}