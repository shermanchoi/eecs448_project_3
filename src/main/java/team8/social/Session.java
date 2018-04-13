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
	 * 
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
	 * 
	 * @param username_in
	 *            The username of the session
	 * @param sessionID_in
	 *            The sessionID of the session
	 * @return True if the method exists, false otherwise
	 */
	public static boolean validate(String sessionID_in, String username_in) {
		String query = "SELECT * FROM social_sessions WHERE sessionID = '" + sessionID_in + "';";
		DatabaseGetter getter = new DatabaseGetter(query);
		ResultSet rs = getter.results;
		try {
			while (rs.next()) {
				if (username_in.equals(rs.getString("username"))) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}
		return false;
	}

	/**
	 * This method allows a session to be created.
	 * 
	 * @param sessionID_in
	 *            The session id
	 * @param username_in
	 *            The session username
	 * @return A session object if created successfully, null otherwise.
	 */
	public static Session createSession(String sessionID_in, String username_in) {
		//Prepare Statement
		DatabaseSetter setter = new DatabaseSetter("INSERT INTO `social_sessions` (`sessionID`,`username`) VALUES (?,?);");
		
		try {
			//Statement preparing.
			setter.statement.setString(1,sessionID_in);
			setter.statement.setString(2,username_in);
			//Execution of statement.
			if (setter.execute()) {
				return new Session(sessionID_in, username_in);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method deletes a session from the database
	 * 
	 * @param sessionID_in
	 *            The ID of the session in question
	 * @param username_in
	 *            The username associated with the session
	 * @return True if the query occurs successfully, false otherwise.
	 */
	public static boolean deleteSession(String sessionID_in, String username_in) {
		String query = "DELETE FROM social_sessions WHERE sessionID='" + sessionID_in + "' AND username='" + username_in
				+ "';";
		return Database.querySQLSet(query);
	}
}