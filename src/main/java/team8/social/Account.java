import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
	 *       changes will be made on the Account object.
	 * @param currentPassword
	 *            The current password of the User.
	 * @param newPassword
	 *            The new password the user wants
	 * @return True if the change occurs, false otherwise.
	 */
	public boolean changePassword(String currentPassword, String newPassword) {
		if (currentPassword == password) {
			password = newPassword;
			return true;
		} else {
			return false;
		}
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
				"UPDATE Accounts SET password=\"" + password + "\",birthday=\"" + birthday + "\",firstName=\""
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
	public static Account createAccount(String uname, String pword, String dateOfBirth, String fName, String lName) {
		if (Database.querySQLSet("INSERT INTO Accounts (username,password,birthday,firstName,lastName) VALUE " + "(\""
				+ uname + "\",\"" + pword + "\",\"" + dateOfBirth + "\",\"" + fName + "\",\"" + lName + "\");")) {
			return new Account(uname, pword, dateOfBirth, fName, lName);
		} else {
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
		Connection connection = Database.connect();
		Account account = null;

		try {
			ResultSet results = null;

			String query = "SELECT * FROM Accounts WHERE username = \"" + username + "\";";

			try {
				System.out.println("Executing Statement:\n\t" + query);
				Statement statement = connection.createStatement();
				results = statement.executeQuery(query);

				while (results.next()) {
					if (password.equals(results.getString("password"))) {
						String dob = results.getString("birthday");
						String fname = results.getString("firstName");
						String lname = results.getString("lastName");
						account = new Account(username, password, dob, fname, lname);
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
		return account;
	}
}