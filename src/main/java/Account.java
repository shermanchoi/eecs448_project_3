import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Account {
	private String username, password, birthday, firstName, lastName;

	public Account(String uname, String pword, String dateOfBirth, String fName, String lName) {
		username = uname;
		password = pword;
		birthday = dateOfBirth;
		firstName = fName;
		lastName = lName;
	}

	public static Account createAccount(String uname, String pword, String dateOfBirth, String fName, String lName) {
		Database.querySQLSet("INSERT INTO Accounts (username,password,birthday,firstName,lastName) VALUE " + "(\""
				+ uname + "\",\"" + pword + "\",\"" + dateOfBirth + "\",\"" + fName + "\",\"" + lName + "\");");
		return new Account(uname, pword, dateOfBirth, fName, lName);
	}

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
					if(password.equals(results.getString("password"))){
						String dob = results.getString("birthday");
						String fname = results.getString("firstName");
						String lname = results.getString("lastName");
						account = new Account(username,password,dob,fname,lname);
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