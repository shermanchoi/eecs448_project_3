
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;

public class TestAccountCreation {

	@After
	public void resetDatabase() {
		//Reset the database everytime account creation is tested.
		Database.hardReset();
	}

	@Test
	public void testValidAccountCreation() {
		//This is a valid account creation instance.
		assertNotNull("Valid Account Creation Failed.",
				Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testTakenUsernameAccountCreation() {
		//This is a valid account creation.
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
		//This should fail because schoi is already used as an username.
		assertNull("Invalid Account Creation Allowed (username already taken).",
				Account.createAccount("schoi", "1234pass", "1987-11-22", "Someone", "NotSherman",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Picasso", "SomewhereOverTheRainbow", "Avatar"));
	}

	@Test
	public void testShortPasswordAccountCreation() {
		//This should fail because passwords cannot be larger than eight characters.
		assertNull("Invalid Account Creation Allowed (password needs to be at least 8 characters).",
				Account.createAccount("dummy", "1234567", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testSameSecurityQuestionsAccountCreation() {
		//This should fail because the security questions cannot be the same.
		assertNull("Invalid Account Creation Allowed (security questions cannot be the same).",
				Account.createAccount("dummy", "12345678", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?",
						"Who is your favorite actor, musician, or artist?", "What is your favorite movie?", "Pendulum",
						"Pendulum", "Toy Story"));
	}

	@Test
	public void testEmptyUsernameAccountCreation() {
		//This should fail because the username cannot be empty.
		assertNull("Invalid Account Creation Allowed (username cannot be empty).",
				Account.createAccount("", "12345678", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyPasswordAccountCreation() {
		//This should fail because passwords cannot be empty.
		assertNull("Invalid Account Creation Allowed (password cannot be empty).",
				Account.createAccount("abc", "", "1997-11-22", "Someone", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyFirstNameAccountCreation() {
		//This should fail because first names cannot be empty.
		assertNull("Invalid Account Creation Allowed (first name cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "", "NotReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyLastNameAccountCreation() {
		//This should fail because last names cannot be empty.
		assertNull("Invalid Account Creation Allowed (last name cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptySecurityQuestionAnswersAccountCreation() {
		//This should fail because the security answer questions cannot be empty
		assertNull("Invalid Account Creation Allowed (security question answers cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "HatesReadingDirections",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "", "", ""));
	}
}
