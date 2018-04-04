import static spark.Spark.*;

import spark.Session;

import static j2html.TagCreator.*;

public class Main {
	public static void main(String[] args) {
		port(4567);

		staticFiles.location("/public");
		init();

		get("/", (req, res) -> {
			res.redirect("/html/login.html");

			System.out.println((String) req.session().attribute("username"));

			return "";
		});
		post("/createAccount", (req, res) -> {
			String username = req.queryParams("uname");
			String password = req.queryParams("pword");
			String dateOfBirth = "1998-12-25";
			String firstName = req.queryParams("fname");
			String lastName = req.queryParams("lname");

			Account a = Account.createAccount(username, password, dateOfBirth, firstName, lastName);

			if (a != null) {
				return "Account Creation Successful!</br><a href=\"/\">Go Back</a>";
			} else {
				return "Account Creation Failure!</br><a href=\"/html/createAccount.html\">Go Back</a>";
			}

		});
		post("/login", (req, res) -> {
			String uname = req.queryParams("username");
			String pword = req.queryParams("password");
			Account a = Account.login(uname, pword);
			if (a != null) {
				req.session(true);
				req.session().attribute("username", uname);

				return "Login Successful!</br><a href=\"/\">Go Back</a>";
			} else {
				res.redirect("/html/login.html");
				return "";
			}

		});
	}
}