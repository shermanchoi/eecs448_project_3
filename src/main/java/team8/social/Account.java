package team8.social;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONStringer;

public class Account {
	/**
	 * Information of the account
	 */
	private String username, password, birthday, firstName, lastName;

	/**
	 * The constructor of an Account class
	 * 
	 * @param uname
	 *            Username of the account
	 * @param pword
	 *            Password of the account
	 * @param dateOfBirth
	 *            Birthday of the user of the account
	 * @param fName
	 *            First name of the user of the account
	 * @param lName
	 *            Last name of the user of the account
	 */
	public Account(String uname, String pword, String dateOfBirth, String fName, String lName) {
		username = uname;
		password = pword;
		birthday = dateOfBirth;
		firstName = fName;
		lastName = lName;
	}

	/**
	 * This method allows password updating, given the user already knows the
	 * password
	 * 
	 * @param username
	 *            The username of the user in question.
	 * @param currentPassword
	 *            The current password of the user.
	 * @param newPassword
	 *            The new password for the user.
	 * @return True if the password occured correctly, false otherwise.
	 */
	public static boolean changePassword(String username, String currentPassword, String newPassword) {
		currentPassword = encryptOneWay(username, currentPassword);
		newPassword = encryptOneWay(username, newPassword);

		// Look at current password first.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

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
				// Check if the password even matches.
				if (!rs.getString("password").equals(currentPassword)) {
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		// Now, change the password.
		// Statement to prepare.
		DatabaseSetter setter = new DatabaseSetter("UPDATE `social_accounts` SET `password`=? WHERE `username`=?;");
		try {
			// Statement preparing.
			setter.statement.setString(1, newPassword);
			setter.statement.setString(2, username);
			// Execution of statement.
			if (setter.execute()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method allows password updating. Old password not needed
	 * 
	 * @param username
	 *            The username in question
	 * @param newPassword
	 *            The new password
	 * @return True if the account password changed successfully
	 */
	public static boolean changePassword(String username, String newPassword) {
		newPassword = encryptOneWay(username, newPassword);

		// Change the password.
		// Statement to prepare.
		DatabaseSetter setter = new DatabaseSetter("UPDATE `social_accounts` SET `password`=? WHERE `username`=?;");
		try {
			// Statement preparing.
			setter.statement.setString(1, newPassword);
			setter.statement.setString(2, username);
			// Execution of statement.
			if (setter.execute()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This checks the username and their security question answers of an user who
	 * wants to reset their password
	 * 
	 * @param username
	 *            The username of the account in question
	 * @param ansQ1
	 *            The answer to the first security question
	 * @param ansQ2
	 *            The answer to the second security question
	 * @param ansQ3
	 *            The answer to the third security question
	 * @return True if everything is correct, false otherwise
	 */
	public static boolean securityQuestionCheck(String username, String ansQ1, String ansQ2, String ansQ3) {

		boolean correctlyAnswered = false;

		ansQ1 = encryptOneWay(username, ansQ1);
		ansQ2 = encryptOneWay(username, ansQ2);
		ansQ3 = encryptOneWay(username, ansQ3);

		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

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
				// The recovery needs everything to be correct!
				correctlyAnswered = rs.getString("securityAnswer1").equals(ansQ1)
						&& rs.getString("securityAnswer2").equals(ansQ2)
						&& rs.getString("securityAnswer3").equals(ansQ3);
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return correctlyAnswered;
	}

	/**
	 * This method changes the personal information of a given account.
	 * 
	 * @param uname
	 *            Username of Account
	 * @param dateOfBirth
	 *            Date of birth of account
	 * @param fName
	 *            First name of account
	 * @param lName
	 *            Last name of account
	 * @param secQ1
	 *            Security question 1
	 * @param secQ2
	 *            Security question 2
	 * @param secQ3
	 *            Security question 3
	 * @param ansQ1
	 *            Security question answer 1
	 * @param ansQ2
	 *            Security question answer 2
	 * @param ansQ3
	 *            Security question answer 3
	 * @return True if successful, false otherwise
	 */
	public static boolean changePersonalInformation(String uname, String dateOfBirth, String fName, String lName,
			String secQ1, String secQ2, String secQ3, String ansQ1, String ansQ2, String ansQ3) {
		// Security questions cannot be the same.
		if (secQ1.equals(secQ2) || secQ2.equals(secQ3) || secQ3.equals(secQ1)) {
			return false;
		}

		// These fields cannot be null.
		if (uname.length() == 0 || fName.length() == 0 || lName.length() == 0 || ansQ1.length() == 0
				|| ansQ2.length() == 0 || ansQ3.length() == 0) {
			return false;
		}

		// Encrypt Sensitive information
		ansQ1 = encryptOneWay(uname, ansQ1);
		ansQ2 = encryptOneWay(uname, ansQ2);
		ansQ3 = encryptOneWay(uname, ansQ3);
		// Escape potentially harmful parameters.
		fName = StringEscapeUtils.escapeHtml4(fName);
		lName = StringEscapeUtils.escapeHtml4(lName);
		secQ1 = StringEscapeUtils.escapeHtml4(secQ1);
		secQ2 = StringEscapeUtils.escapeHtml4(secQ2);
		secQ3 = StringEscapeUtils.escapeHtml4(secQ3);

		// Statement to prepare.
		DatabaseSetter setter = new DatabaseSetter(
				"UPDATE `social_accounts` SET `birthday`= ?,`firstName`= ?,`lastName`= ?,`securityQuestion1`=?,`securityQuestion2`= ?,`securityQuestion3`=?,`securityAnswer1`=?,`securityAnswer2`=?,`securityAnswer3`=? WHERE `username`=?;");

		try {
			// Statement preparing.
			setter.statement.setString(1, dateOfBirth);
			setter.statement.setString(2, fName);
			setter.statement.setString(3, lName);
			setter.statement.setString(4, secQ1);
			setter.statement.setString(5, secQ2);
			setter.statement.setString(6, secQ3);
			setter.statement.setString(7, ansQ1);
			setter.statement.setString(8, ansQ2);
			setter.statement.setString(9, ansQ3);
			setter.statement.setString(10, uname);
			// Execution of statement.
			if (setter.execute()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method returns a json object with the security questions of a user
	 * 
	 * @param username
	 *            The username of the user in question
	 * @return A JSON string representing the security questions.
	 */
	public static String getSecurityQuestions(String username) {
		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		String json = null;
		DatabaseGetter getter = new DatabaseGetter(query);

		try {
			// Prepare the statement
			getter.statement.setString(1, username);
			// Execute statement.
			getter.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ResultSet rs = getter.results;

		try {
			while (rs.next()) {
				// Build the object
				json = new JSONStringer().object() // Start object
						.key("username").value(username) // Username in question
						.key("securityQuestion1").value(rs.getString("securityQuestion1")) // Security Question 1
						.key("securityQuestion2").value(rs.getString("securityQuestion2")) // Security Question 2
						.key("securityQuestion3").value(rs.getString("securityQuestion3")) // Security Question 3
						.endObject().toString(); // end object
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return json;
	}

	/**
	 * Get username method
	 * 
	 * @return username of this account
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get birthday method
	 * 
	 * @return birthday of the account user.
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * Get first name method
	 * 
	 * @return first name of the account user.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Get last name method
	 * 
	 * @return last name of the account user.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This method creates a new account and returns it as a new Account object.
	 * 
	 * @pre The account information to be used must be valid.
	 * @post The account is created.
	 * @param uname
	 *            Username of the new account
	 * @param pword
	 *            Password of the new account
	 * @param dateOfBirth
	 *            Birthday of the user of the new account
	 * @param fName
	 *            First name of the user of the new account
	 * @param lName
	 *            Last name of the user of the new account
	 * @return An Account object representing the new account if placed without
	 *         error, null otherwise
	 */
	public static Account createAccount(String uname, String pword, String dateOfBirth, String fName, String lName,
			String secQ1, String secQ2, String secQ3, String ansQ1, String ansQ2, String ansQ3) {

		// Password cannot be less than 8 characters.
		if (pword.length() < 8) {
			return null;
		}
		// Security questions cannot be the same.
		if (secQ1.equals(secQ2) || secQ2.equals(secQ3) || secQ3.equals(secQ1)) {
			return null;
		}

		// These fields cannot be null.
		if (uname.length() == 0 || fName.length() == 0 || lName.length() == 0 || ansQ1.length() == 0
				|| ansQ2.length() == 0 || ansQ3.length() == 0) {
			return null;
		}

		// Usernames should not need to be escaped by HTML characters.
		if (!uname.equals(StringEscapeUtils.escapeHtml4(uname))) {
			return null;
		}

		// Encrypt Sensitive information
		pword = encryptOneWay(uname, pword);
		ansQ1 = encryptOneWay(uname, ansQ1);
		ansQ2 = encryptOneWay(uname, ansQ2);
		ansQ3 = encryptOneWay(uname, ansQ3);
		// Escape potentially harmful parameters.
		fName = StringEscapeUtils.escapeHtml4(fName);
		lName = StringEscapeUtils.escapeHtml4(lName);
		secQ1 = StringEscapeUtils.escapeHtml4(secQ1);
		secQ2 = StringEscapeUtils.escapeHtml4(secQ2);
		secQ3 = StringEscapeUtils.escapeHtml4(secQ3);

		// Statement to prepare.
		DatabaseSetter setter = new DatabaseSetter("INSERT INTO `social_accounts`(`username`,`password`,"
				+ "`birthday`,`firstName`,`lastName`,`securityQuestion1`,`securityQuestion2`,"
				+ "`securityQuestion3`,`securityAnswer1`,`securityAnswer2`,`securityAnswer3`)VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?);");

		try {
			// Statement preparing.
			setter.statement.setString(1, uname);
			setter.statement.setString(2, pword);
			setter.statement.setString(3, dateOfBirth);
			setter.statement.setString(4, fName);
			setter.statement.setString(5, lName);
			setter.statement.setString(6, secQ1);
			setter.statement.setString(7, secQ2);
			setter.statement.setString(8, secQ3);
			setter.statement.setString(9, ansQ1);
			setter.statement.setString(10, ansQ2);
			setter.statement.setString(11, ansQ3);
			// Execution of statement.
			if (setter.execute()) {
				return new Account(uname, pword, dateOfBirth, fName, lName);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method uses an username and password pair and checks the data base if
	 * they exist.
	 * 
	 * @param username
	 *            The username of the account
	 * @param password
	 *            The password of the account
	 * @return An Account object representing the account if it exists. Null
	 *         otherwise.
	 */
	public static Account login(String username, String password) {
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

		try {
			// Prepare the statement
			getter.statement.setString(1, username);
			// Execute statement.
			getter.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ResultSet rs = getter.results;
		Account account = null;

		// Check against digested stuff.
		password = encryptOneWay(username, password);

		try {
			while (rs.next()) {
				if (rs.getString("password").equals(password)) {
					String dob = rs.getString("birthday");
					String fname = rs.getString("firstName");
					String lname = rs.getString("lastName");
					account = new Account(username, password, dob, fname, lname);
				}
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return account;
	}

	/**
	 * This returns the profile page information of an user.
	 * 
	 * @pre The account with the username exists
	 * @param username
	 *            The username of the user in question
	 * @return The JSON representing the user's profile information.
	 */
	public static String getProfilePageInformation(String username) {
		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

		try {
			// Prepare the statement
			getter.statement.setString(1, username);
			// Execute statement.
			getter.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ResultSet rs = getter.results;
		String json = null;

		try {
			while (rs.next()) {
				json = new JSONStringer().object() // Start object
						.key("username").value(rs.getString("username")) // Username
						.key("firstName").value(rs.getString("firstName")) // First name
						.key("lastName").value(rs.getString("lastName")) // Last name
						.key("birthday").value(rs.getString("birthday")) // Birthday of user
						.key("biography").value(rs.getString("biography")) // biography of user
						.key("adminStatus").value(rs.getInt("adminStatus")) // admin status (0 or 1)
						.endObject().toString(); // End object.
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return json;
	}

	public static boolean isBanned(String username) {
		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

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
				return (1 == rs.getInt("banned"));
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}
		return false;
	}

	/**
	 * This returns the account management page information of an user.
	 * 
	 * @pre The account with the username exists
	 * @param username
	 *            The username of the user in question
	 * @return The JSON representing the user's account management page information.
	 */
	public static String getAccountManagementInformation(String username) {
		// Get the query ready.
		String query = "SELECT * FROM social_accounts WHERE username=?;";
		DatabaseGetter getter = new DatabaseGetter(query);

		try {
			// Prepare the statement
			getter.statement.setString(1, username);
			// Execute statement.
			getter.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ResultSet rs = getter.results;
		String json = null;

		try {
			while (rs.next()) {
				json = new JSONStringer().object() // Start object
						.key("firstName").value(rs.getString("firstName")) // First name
						.key("lastName").value(rs.getString("lastName")) // Last name
						.key("birthday").value(rs.getString("birthday")) // Birthday of user
						.key("biography").value(rs.getString("biography")) // biography of user
						.key("securityQuestion1").value(rs.getString("securityQuestion1")) // security question 1 of user
						.key("securityQuestion2").value(rs.getString("securityQuestion2")) // security question 2 of user
						.key("securityQuestion3").value(rs.getString("securityQuestion3")) // security question 3 of user
						.endObject().toString(); // End object.
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return json;
	}

	/**
	 * Encryption helper method. Meant for one-way sensitive information encryption.
	 * 
	 * @pre The key is an unique element.
	 * @param key
	 *            The item that is guaranteed to be unique, like username.
	 * @param salt
	 *            The item that needs to be encrypted, like password.
	 * @return A string that as been encrypted using SHA-512
	 */
	private static String encryptOneWay(String key, String salt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			// The key is unique, but the second part is not, so let it act as the actual
			// salt.
			String encrypted = key + Hex.encodeHexString(md.digest((key + salt).getBytes()));
			// Re-encrypt it
			return Hex.encodeHexString(md.digest(encrypted.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}