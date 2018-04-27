package team8.tests;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;

public class TestAccountCreation {

	@BeforeClass
	public static void setupDatabase() {
		// Reset the database
		Database.hardReset();
	}

	@After
	public void resetDatabase() {
		// Reset the database everytime account creation is tested.
		Database.hardReset();
	}

	@Test
	public void testValidAccountCreation() {
		// This is a valid account creation instance.
		assertNotNull("Valid Account Creation Failed.", Account.createAccount("schoi", "1234pass", "1997-11-22",
				"Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testTakenUsernameAccountCreation() {
		// This is a valid account creation.
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// This should fail because schoi is already used as an username.
		assertNull("Invalid Account Creation Allowed (username already taken).",
				Account.createAccount("schoi", "1234pass", "1987-11-22", "Someone", "NotSherman", "q1", "q2", "q3",
						"Picasso", "SomewhereOverTheRainbow", "Avatar"));
	}

	@Test
	public void testShortPasswordAccountCreation() {
		// This should fail because passwords cannot be larger than eight characters.
		assertNull("Invalid Account Creation Allowed (password needs to be at least 8 characters).",
				Account.createAccount("dummy", "1234567", "1997-11-22", "Someone", "NotReadingDirections", "q1", "q2",
						"q3", "Pendulum", "SME", "Toy Story"));
	}

	@Test
	public void testSameSecurityQuestionsAccountCreation() {
		// This should fail because the security questions cannot be the same.
		assertNull("Invalid Account Creation Allowed (security questions cannot be the same).",
				Account.createAccount("dummy", "12345678", "1997-11-22", "Someone", "NotReadingDirections", "q1", "q1",
						"q3", "Pendulum", "Pendulum", "Toy Story"));
	}

	@Test
	public void testEmptyUsernameAccountCreation() {
		// This should fail because the username cannot be empty.
		assertNull("Invalid Account Creation Allowed (username cannot be empty).",
				Account.createAccount("", "12345678", "1997-11-22", "Someone", "NotReadingDirections", "q1", "q2", "q3",
						"Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyPasswordAccountCreation() {
		// This should fail because passwords cannot be empty.
		assertNull("Invalid Account Creation Allowed (password cannot be empty).",
				Account.createAccount("abc", "", "1997-11-22", "Someone", "NotReadingDirections", "q1", "q2", "q3",
						"Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyFirstNameAccountCreation() {
		// This should fail because first names cannot be empty.
		assertNull("Invalid Account Creation Allowed (first name cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "", "NotReadingDirections", "q1", "q2", "q3",
						"Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptyLastNameAccountCreation() {
		// This should fail because last names cannot be empty.
		assertNull("Invalid Account Creation Allowed (last name cannot be empty).", Account.createAccount("abc",
				"12345678", "1997-11-22", "Johnny", "", "q1", "q2", "q3", "Pendulum", "Somewhere", "Toy Story"));
	}

	@Test
	public void testEmptySecurityQuestionAnswersAccountCreation() {
		// This should fail because the security answer questions cannot be empty
		assertNull("Invalid Account Creation Allowed (security question answers cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "HatesReadingDirections", "q1", "q2",
						"q3", "", "", ""));
	}
	
	@Test
	public void testInvalidSecurityQuestionsAccountCreation() {
		// This should fail because the security answer questions cannot be empty
		assertNull("Invalid Account Creation Allowed (security question answers cannot be empty).",
				Account.createAccount("abc", "12345678", "1997-11-22", "Johnny", "HatesReadingDirections", "q2345654321", "HAHAHAHHAHAHHAHAHAH",
						"Nope", "John", "Sir", "Madam"));
	}

	@Test
	public void testSQLAttackAttemptUsername() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;", "1234pass",
				"1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME", "Toy Story");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (Username field not protected)", Account.login("schoi", "1234pass"));
	}

	@Test
	public void testSQLAttackAttemptPassword() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi", "1234pass; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;",
				"1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME", "Toy Story");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (Password field not protected)", Account.login("schoi", "1234pass"));
	}

	@Test
	public void testSQLAttackAttemptBirthday() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi", "1234pass",
				"1997-11-22; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;", "Sherman", "Choi",
				"q1", "q2", "q3", "Pendulum", "SME", "Toy Story");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (Birthday field not protected)", Account.login("schoi", "1234pass"));
	}

	@Test
	public void testSQLAttackAttemptFirstName() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi", "1234pass", "1997-11-22",
				"Sherman; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;", "Choi", "q1", "q2",
				"q3", "Pendulum", "SME", "Toy Story");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (First Name field not protected)",
				Account.login("schoi", "1234pass"));
	}

	@Test
	public void testSQLAttackAttemptLastName() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman",
				"Choi; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;", "q1", "q2", "q3",
				"Pendulum", "SME", "Toy Story");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (Last Name field not protected)", Account.login("schoi", "1234pass"));
	}

	@Test
	public void testSQLAttackAttemptSecurityAnswers() {
		// This is a valid account creation
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		// Malicious attack attempt
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3",
				"Pendulum; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;",
				"SME; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;",
				"Toy Story; DROP TABLE IF EXISTS social_sessions, social_posts, social_accounts;");
		// The account is still there? Check.
		assertNotNull("SQL Attack deleted tables. (Security Question Answers field(s) not protected)",
				Account.login("schoi", "1234pass"));
	}
}
