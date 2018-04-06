package team8.social;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Session {
	/**
	 * The ID of the session.
	 */
	private int sessionID;
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
	public Session(int sessionID_in, String username_in) {
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
	public boolean validate(String username_in, int sessionID_in) {
		Connection connection = Database.connect();
		boolean found = false;

		try {
			ResultSet results = null;

			String query = "SELECT * FROM Sessions WHERE sessionID = \"" + sessionID_in + "\";";

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
		Database.querySQLSet("DELETE FROM Sessions WHERE sessionID='" + sessionID + "'");
	}
	
}