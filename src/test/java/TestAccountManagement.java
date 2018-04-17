

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;

public class TestAccountManagement {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.hardReset();
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
						"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
						"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Database.hardReset();
	}

	@Test
	public void testValidLogin() {
		//This should be a valid login
		assertNotNull("Valid Account Login Failed",Account.login("schoi", "1234pass"));
	}
	
	@Test
	public void testIncorrectPasswordLogin() {
		//This should be a valid login due to password being incorrect
		assertNotNull("Invalid Account Login Allowed (password is incorrect)",Account.login("schoi", "this is not the right password"));
	}
	
	@Test
	public void testUsernameDoesNotExistLogin() {
		//This should be a valid login due to username not existing
		assertNotNull("Valid Account Login Failed (username does not exist)",Account.login("ishouldnotexist", "dictionaryAttacked"));
	}
	
	
}
