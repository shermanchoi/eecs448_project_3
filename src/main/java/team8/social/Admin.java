package team8.social;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

public class Admin {
	/**
	 * This method gives one admin status or removes it.
	 * 
	 * @param username
	 *            The username of the account in question.
	 * @param isAdmin
	 *            The status of the account post activation.
	 * @return True if everything works, false otherwise
	 */
	public static boolean setAdminStatus(String username, boolean setAdmin) {
		//Does the user even exist?
		boolean found = false;
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);
		
		try {
			//Prepare the statement
			getter.statement.setString(1,username);
			//Execute statement.
			getter.execute();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		ResultSet rs = getter.results;
		try {
			while (rs.next()) {
				found = true;
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}
		
		if(!found) {
			return false;
		}
		
		
		//The user exists.
		DatabaseSetter setter = new DatabaseSetter("UPDATE `social_accounts` SET `adminStatus`=? WHERE `username`=?;");
		
		// Statement preparing.
		try {
			setter.statement.setBoolean(1, setAdmin);
			setter.statement.setString(2, username);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Execution of statement.
		if (setter.execute()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This returns information for the admin to see.
	 * 
	 * @param username
	 *            THe user requesting the JSON
	 * @return JSON to see all the information an admin may want if they are an
	 *         admin, otherwise they get back nonsense.
	 */
	public static String getAdminViewJSON(String username) {
		if (isAdmin(username)) {

			JSONArray users = new JSONArray();

			// Get the query ready.
			String query = "SELECT * FROM social_accounts;";
			DatabaseGetter getter = new DatabaseGetter(query);

			try {
				getter.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			ResultSet rs = getter.results;

			try {
				while (rs.next()) {
					users.put(rs.getString("username"));
				}
			} catch (Exception e) {
				System.out.println("ResultSet Error:\n\t" + e.getMessage());
			}

			JSONObject adminView = new JSONObject().put("users", users);

			return adminView.toString();
		} else {
			return new JSONObject().put("users", "stop trying to hack us").toString();
		}
	}

	/**
	 * This method allows the admin to change the password of an user.
	 * 
	 * @param username
	 *            The user requesting the change
	 * @param who
	 *            The user who will have the password change
	 * @param newPassword
	 *            The new password of the user
	 * @return True if the password change happens, false otherwise.
	 */
	public static boolean changeOtherPassword(String username, String who, String newPassword) {
		if (isAdmin(username)) {
			// Alright, change the password
			return Account.changePassword(who, newPassword);
		} else {
			// This is not an admin!
			return false;
		}
	}

	/**
	 * This method allows the admin to ban an user.
	 * 
	 * @param username
	 *            The user requesting the ban
	 * @param who
	 *            The user who will be banned
	 * @return True if the ban happens, false otherwise.
	 */
	public static boolean banUser(String username, String who, boolean banned) {
		if (isAdmin(username)) {
			DatabaseSetter setter = new DatabaseSetter(
					"UPDATE `social_accounts` SET `banned` = ? WHERE `username` = ?;");

			// Statement preparing.
			try {
				if(banned) {
					setter.statement.setInt(1, 1);
				}else {
					setter.statement.setInt(1, 0);
				}
				setter.statement.setString(2, who);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Execution of statement.
			if (setter.execute()) {
				return true;
			} else {
				return false;
			}
		} else {
			// This is not an admin!
			return false;
		}
	}

	/**
	 * This method helps by checking if an user is an admin.
	 * 
	 * @param username
	 *            The user in question.
	 * @return True if the user is an admin, false otherwise
	 */
	public static boolean isAdmin(String username) {
		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

		boolean isAdmin = false;

		try {
			// Prepare the statement
			getter.statement.setString(1, username);
			// Execute statement.
			getter.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		ResultSet rs = getter.results;

		try {
			while (rs.next()) {
				// see a 1? This user is an admin.
				isAdmin = rs.getInt("adminStatus") == 1;
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return isAdmin;
	}

	/**
	 * This method deletes a post and its replies.
	 * 
	 * @post 
	 * @param username
	 *            The user trying to do the action.
	 * @param postID
	 *            The id of the post to delete.
	 * @return True if it occurred without error, false otherwise.
	 */
	public static boolean removePost(String username, int postID) {
		if (isAdmin(username)) {
			//Look for the post in question
			boolean found = false;
			String query = "SELECT * FROM social_posts WHERE id=?;";
			DatabaseGetter getter = new DatabaseGetter(query);
			
			try {
				//Prepare the statement
				getter.statement.setInt(1,postID);
				//Execute statement.
				getter.execute();
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
			ResultSet rs = getter.results;
			try {
				while (rs.next()) {
					found = true;
				}
			} catch (Exception e) {
				System.out.println("ResultSet Error:\n\t" + e.getMessage());
			}
			
			//The post is not even found.
			if(!found) {
				return false;
			}
			
			// Get the queries ready.
			String queryRepliesDelete = "DELETE FROM social_posts WHERE parentPost=?;";
			String queryPostDelete = "DELETE FROM social_posts WHERE id=?;";

			DatabaseSetter setterRepliesDelete = new DatabaseSetter(queryRepliesDelete);

			try {
				// Prepare the statement
				setterRepliesDelete.statement.setInt(1, postID);
				// Execute statement.
				setterRepliesDelete.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			DatabaseSetter setterPostDelete = new DatabaseSetter(queryPostDelete);

			try {
				// Prepare the statement
				setterPostDelete.statement.setInt(1, postID);
				// Execute statement.
				setterPostDelete.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		} else {
			return false;
		}
	}
}