
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;

public class TestSuite {

	@Before
	public void newlyCreatedDatabase() {
		Database.hardReset();
	}

	@Test
	public void testValidAccountCreation() {
		assertNotNull("Valid Account Creation Failed.",
				Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testTakenUsernameAccountCreation() {
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
		assertNull("Invalid Account Creation Allowed (username already taken).",
				Account.createAccount("schoi", "1234pass", "1987-11-22", "Someone", "NotSherman",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Picasso", "SomewhereOverTheRainbow", "Avatar"));
	}

	@Test
	public void testShortPasswordAccountCreation() {
		assertNull("Invalid Account Creation Allowed (password needs to be at least 8 characters).",
				Account.createAccount("dummy", "1234567", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testSameSecurityQuestionsAccountCreation() {
		assertNull("Invalid Account Creation Allowed (security questions cannot be the same).",
				Account.createAccount("dummy", "12345678", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?",
						"Who is your favorite actor, musician, or artist?", "What is your favorite movie?", "Pendulum",
						"Pendulum", "Toy Story"));
	}

	@Test
	public void testEmptyUsernameAccountCreation() {
		assertNull("Invalid Account Creation Allowed (username cannot be empty).",
				Account.createAccount("", "12345678", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyPasswordAccountCreation() {
		assertNull("Invalid Account Creation Allowed (password cannot be empty).",
				Account.createAccount("abc", "", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyFirstNameAccountCreation() {
		assertNull("Invalid Account Creation Allowed (first name cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyLastNameAccountCreation() {
		assertNull("Invalid Account Creation Allowed (last name cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptySecurityQuestionAnswersAccountCreation() {
		assertNull("Invalid Account Creation Allowed (security question answers cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "HatesReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "", "", ""));
	}

	@Test
	public void testLogin() {
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
		assertNotNull("Valid Account Login Failed.", Account.login("schoi", "1234pass"));
	}
}
