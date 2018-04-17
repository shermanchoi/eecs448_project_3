

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team8.social.Account;

public class TestSuite {

	@Test
	public void accountExists() {
		assertNotNull("Account does not exist", Account.login("ABC", "123"));
	}

}
