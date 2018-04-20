
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;

public class TestAdminFunctionalities {

	@BeforeClass
	public static void setUp() {
		// Reset the database
		Database.hardReset();
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
		Account.createAccount("johnny", "12345678", "1995-08-12", "Joseph", "Something",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Cars", "Did not have to go to school", "Batman");
		Account.createAccount("someone", "abetterpassword123123", "1995-08-12", "Yuria", "Lastname",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "I just like rock", "Something University", "I did not have movies");
		
	}

	@Test
	public void testValidAccountCreation() {
		// This is a valid account creation instance.
		assertNotNull("Valid Account Creation Failed.",
				Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story"));
	}

}
