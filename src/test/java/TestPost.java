import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import team8.social.Account;
import team8.social.Database;
import team8.social.Post;

public class TestPost {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.hardReset();
		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Pendulum", "SME", "Toy Story");
		Account.createAccount("nada", "12345678", "1993-3-12", "Johnny", "Smith",
				"Who is your favorite actor, musician, or artist?", "What high school did you attend?",
				"What is your favorite movie?", "Aerosmith", "SMN", "Avatar");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Database.hardReset();
	}

	@Test
	public void testValidPostCreation() {
		// Valid post.
		assertNotNull("Valid Post creation failed", Post.createPost("nada", "I can decide later.", "Procrastination"));
	}

	@Test
	public void testNoTitlePostCreation() {
		// Invalid post.
		assertNull("Invalid post allowed (Title missing)", Post.createPost("nada", "No, I should not!", ""));
	}

	@Test
	public void testNoContentPostCreation() {
		// Invalid post.
		assertNull("Invalid post allowed (Content missing)",
				Post.createPost("nada", "", "I should stop procrastinating."));
	}

	@Test
	public void testReplyToPost() {
		// Reply to post.
		assertNotNull("Valid Post Reply failed",
				Post.createPost("schoi", "I am not sure what to say, but Hi!.", "doesnotmatter", 1));
	}

	@Test
	public void testNoContentReplyToPost() {
		// Invalid reply to post.
		assertNull("Invalid Post Reply allowed (Content missing)", Post.createPost("schoi", "", "???", 1));
	}

	@Test
	public void testIDDoesNotExistReplyToPost() {
		// Invalid reply to post.
		assertNull("Invalid Post Reply allowed (ID does not exist)", Post.createPost("schoi", "", "???", 2879182));
	}
}
