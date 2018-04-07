package team8.social;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Session {
	/**
	 * The ID of the session.
	 */
	private String sessionID;
	/**
	 * The user associated with the session
	 */
	private String username;

	/**
	 * This is the session constructor
	 * @post Creates the Session in Database.
	 * @param sessionID_in
	 *            The sessionID of the session
	 * @param username_in
	 *            The username of the session
	 */
	public Session(String sessionID_in, String username_in) {
		sessionID = sessionID_in;
		username = username_in;
	}
	/**
	 * This method checks if this session exists in the database.
	 * @param username_in
	 *            The username of the session
	 * @param sessionID_in
	 *            The sessionID of the session
	 * @return True if the method exists, false otherwise
	 */
	public static boolean validate(String username_in, String sessionID_in) {
		Connection connection = Database.connect();
		boolean found = false;

		try {
			ResultSet results = null;

			String query = "SELECT * FROM social_sessions WHERE sessionID = \"" + sessionID_in + "\";";

			try {
				System.out.println("Executing Statement:\n\t" + query);
				Statement statement = connection.createStatement();
				results = statement.executeQuery(query);

				while (results.next()) {
					if (username_in.equals(results.getString("username"))) {
						found = true;
					}
				}

				results.close();
				statement.close();
				Database.disconnect(connection);
				System.out.println("Execution Success");
			} catch (Exception e) {
				System.out.println("Query Error:\n\t" + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Login Error:\n\t" + e.getMessage());
		}

		Database.disconnect(connection);
		return found;
	}
	/**
	 * The method deletes the session's entry in the database.
	 */
	public void remove() {
		Database.querySQLSet("DELETE FROM social_sessions WHERE sessionID='" + sessionID + "'");
	}
	/**
	 * This method allows a session to be created.
	 * @param sessionID_in
	 * The session id
	 * @param username_in
	 * The session username
	 * @return A session object if created successfully, null otherwise.
	 */
	public static Session createSession(String sessionID_in, String username_in){
		try {
			Database.querySQLSet("INSERT INTO `social_sessions`" + 
					"(`sessionID`," + 
					"`username`)" + 
					"VALUES" + "('" + 
					sessionID_in + "','" + 
					username_in + "');");
			return new Session(sessionID_in, username_in);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
}