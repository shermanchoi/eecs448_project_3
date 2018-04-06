package team8.social;

public class Post {
	/**
	 * Information of the Post
	 */
	private String author, message, title;

	/**
	 * Constructor of a Post object
	 * 
	 * @param inputAuthor
	 *            The username of the author of the post
	 * @param inputMessage
	 *            The message or content inside the post
	 * @param inputTitle
	 *            The title of the post
	 */
	public Post(String inputAuthor, String inputMessage, String inputTitle) {
		author = inputAuthor;
		message = inputMessage;
		title = inputTitle;
	}

	/**
	 * This method creates a new post and returns it as a new Post object.
	 * 
	 * @pre The post information to be used must be valid.
	 * @post The post is created.
	 * @param inputAuthor
	 *            The username of the author of the post
	 * @param inputMessage
	 *            The message or content inside the post
	 * @param inputTitle
	 *            The title of the post
	 * @return A new Post object representing the new post is return if everything
	 *         is valid, otherwise null
	 */
	public static Post createPost(String inputAuthor, String inputMessage, String inputTitle) {
		if (Database.querySQLSet("INSERT INTO `sys`.`Posts` (`author`,`message`,`title`)"
				+ "VALUES" + "(" + inputAuthor + "," + inputMessage + "," + inputTitle + ");")) {
			return new Post(inputAuthor, inputMessage, inputTitle);
		} else {
			return null;
		}
	}

	/**
	 * This method creates a new post and returns it as a new Post object. This one
	 * specifically generates a reply to an existing message
	 * 
	 * @pre The post information to be used must be valid.
	 * @post The post is created.
	 * @param inputAuthor
	 *            The username of the author of the post
	 * @param inputMessage
	 *            The message or content inside the post
	 * @param inputTitle
	 *            The title of the post
	 * @param replyingToPostID
	 *            The id of the post to reply to
	 * @return A new Post object representing the new post is return if everything
	 *         is valid, otherwise null
	 */
	public static Post createPost(String inputAuthor, String inputMessage, String inputTitle, int replyingToPostID) {
		if (Database.querySQLSet("INSERT INTO `sys`.`Posts`" + "(`author`," + "`message`," + "`title`," + "`parentPost`)"
				+ "VALUES" + "(" + inputAuthor + "," + inputMessage + "," + inputTitle + "," + replyingToPostID + ");")) {
			return new Post(inputAuthor, inputMessage, inputTitle);
		} else {
			return null;
		}
	}

}