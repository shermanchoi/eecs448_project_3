package team8.social;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

public class Admin {

	public static boolean setAdminStatus(String username, boolean isAdmin) {
		DatabaseSetter setter = new DatabaseSetter("UPDATE `social_accounts` SET `adminStatus`=? WHERE `username`=?;");

		// Statement preparing.
		try {
			if (isAdmin) {
				// Give admin status
				setter.statement.setInt(1, 1);
			} else {
				// Remove admin status
				setter.statement.setInt(1, 0);
			}
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
			JSONObject posts = JSONML.toJSONObject(Post.JSONAllPosts());

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

			JSONObject adminView = new JSONObject().put("users", users).put("posts", posts);

			return adminView.toString();
		} else {
			return new JSONObject().put("users", "stop trying to hack us").put("posts", "seriously stop").toString();
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
	public static boolean banUser(String username, String who) {
		if (isAdmin(username)) {
			DatabaseSetter setter = new DatabaseSetter(
					"UPDATE `social_accounts` SET `banned` = 1 WHERE `username` = ?;");

			// Statement preparing.
			try {
				setter.statement.setString(1, who);
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
	private static boolean isAdmin(String username) {
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
}