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
	 * This method changes an user's password
	 * 
	 * @post If the password change is successful and saveImmediately is true,
	 *       changes will be saved in the database and Account object.
	 * @param currentPassword
	 *            The current password of the User.
	 * @param newPassword
	 *            The new password the user wants
	 * @param saveImmediately
	 *            If true, changes will occur immediately within the database.
	 * @return True if the change occurs, false otherwise.
	 */
	public boolean changePassword(String currentPassword, String newPassword, boolean saveImmediately) {
		currentPassword = encryptOneWay(username, currentPassword);
		currentPassword = encryptOneWay(username, newPassword);

		if (currentPassword == password) {
			password = newPassword;
			if (saveImmediately) {
				save();
			}
			return true;
		} else {
			return false;
		}
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
	 * This method saves the account's information into the database.
	 * 
	 * @post The account's information will be altered in the database.
	 */
	private void save() {
		Database.querySQLSet(
				"UPDATE social_accounts SET password=\"" + password + "\",birthday=\"" + birthday + "\",firstName=\""
						+ firstName + "\",lastName=\"" + lastName + "\" WHERE username=\"" + username + "\";");
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

		// Password cannot be less than 8 characters.
		if (pword.length() < 8) {
			return null;
		}
		// Security questions cannot be the same.
		if (secQ1.equals(secQ2) || secQ2.equals(secQ3) || secQ3.equals(secQ1)) {
			return null;
		}

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