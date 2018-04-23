
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import team8.social.Account;
import team8.social.Admin;
import team8.social.Database;
import team8.social.Post;

public class TestAdminFunctionalities {

	@BeforeClass
	public static void setUp() {
		// Reset the database
		Database.hardReset();

		Account.createAccount("schoi", "1234pass", "1997-11-22", "Sherman", "Choi", "q1", "q2", "q3", "Pendulum", "SME",
				"Toy Story");
		Account.createAccount("johnny", "12345678", "1995-08-12", "Joseph", "Something", "q1", "q2", "q3", "Cars",
				"Did not have to go to school", "Batman");
		Account.createAccount("someone", "abetterpassword123123", "1995-08-12", "Yuria", "Lastname", "q1", "q2", "q3",
				"I just like rock", "Something University", "I did not have movies");
	}

	@Before
	public void deletePosts() {
		Post.deleteAllPosts();
	}

	@Test
	public void testIsAdmin() {
		//By default, you are not an admin.
		assertFalse("Admin status given by default", Admin.isAdmin("johnny"));
	}

	@Test
	public void testAdminStatusGranted() {
		// Give these people admin status.
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("someone", true);
		// Do they have admin status? (Except Johnny)
		assertTrue("Admin status granting failed",
				!Admin.isAdmin("johnny") && Admin.isAdmin("schoi") && Admin.isAdmin("someone"));
	}

	@Test
	public void testAdminStatusGrantedAndRemoved() {
		// Give everyone admin status.
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", true);
		Admin.setAdminStatus("someone", true);

		// Remove admin status from everyone but schoi
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);

		assertTrue("Admin status granting then removal failed",
				!Admin.isAdmin("johnny") && Admin.isAdmin("schoi") && !Admin.isAdmin("someone"));
	}

	@Test
	public void testInvalidAdminStatusGranting() {
		// try to give joseph admin status, despite the fact that joseph does not exist.
		assertFalse("Admin status granting then removal failed", Admin.setAdminStatus("joseph", true));
	}

	@Test
	public void testAdminChangeOtherPassword() {
		// Give schoi admin status. (No one else has admin status)
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);
		// Change someone's password
		Admin.changeOtherPassword("schoi", "someone", "qwertyuiop[]\789+");
		// Does the new password work?
		assertNotNull("Admin changing other's password failed.", Account.login("someone", "qwertyuiop[]\789+"));
	}

	@Test
	public void testAdminChangeOtherPasswordNotAdmin() {
		// Give schoi admin status. (No one else has admin status)
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);
		// johnny tries to change someone's password
		Admin.changeOtherPassword("johnny", "someone", "asdfghjkl;'");
		// The password should not change.
		assertNull("Admin changing other's password failed.", Account.login("someone", "asdfghjkl;'"));
	}

	@Test
	public void testAdminDeletePostsWithoutReplies() {
		// Give schoi admin status. (No one else has admin status)
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);

		// This represents a clean webpage.
		String emptiness = Post.JSONAllPosts();

		// Create posts
		Post.createPost("johnny", "Content Here", "TitleHere"); // Id 1
		Post.createPost("someone", "Content Here2", "TitleHere2"); // id 2
		Post.createPost("schoi", "Content There!", "Title Where?"); // id 3
		// Remove posts
		Admin.removePost("schoi", 1);
		Admin.removePost("schoi", 2);
		Admin.removePost("schoi", 3);

		// This should be equal to empty if everything works.
		String postRemovals = Post.JSONAllPosts();

		assertTrue("Posts without replies removals failed", postRemovals.equals(emptiness));
	}

	@Test
	public void testAdminDeletePostsWithReplies() {
		// Give schoi admin status. (No one else has admin status)
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);

		// This represents a clean webpage.
		String emptiness = Post.JSONAllPosts();

		// Create posts
		Post.createPost("johnny", "Content Here", "Title Here"); // Id 1
		Post.createPost("someone", "Content There", "Title There", 1); // id 2
		Post.createPost("schoi", "Content Where", "Title Where", 1); // id 3

		// Remove parent post, which should delete the entire thing.
		Admin.removePost("schoi", 1);

		// This should be equal to empty if everything works.
		String postRemovals = Post.JSONAllPosts();

		assertTrue("Posts with replies removals failed", postRemovals.equals(emptiness));
	}

	@Test
	public void testAdminDeletePostsNotAdmin() {
		// Give schoi admin status. (No one else has admin status)
		Admin.setAdminStatus("schoi", true);
		Admin.setAdminStatus("johnny", false);
		Admin.setAdminStatus("someone", false);

		// Create posts
		Post.createPost("johnny", "Content Here", "TitleHere"); // Id 1
		Post.createPost("someone", "Content Here2", "TitleHere2"); // id 2

		// This represents the webpage with the first two posts.
		String firstTwoPosts = Post.JSONAllPosts();

		// Create the last post.
		Post.createPost("schoi", "Content There!", "Title Where?"); // id 3

		Admin.removePost("johnny", 1); // Remove post attempt by non-admin
		Admin.removePost("someone", 2); // Remove post attempt by non-admin
		Admin.removePost("schoi", 3); // Remove post attempt by admin

		// This should only have the first two posts if this works.
		String postRemovals = Post.JSONAllPosts();

		assertTrue("Posts removal with non-admin allowed", postRemovals.equals(firstTwoPosts));
	}
}
