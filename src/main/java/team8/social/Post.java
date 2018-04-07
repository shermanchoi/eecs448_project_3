package team8.social;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONStringer;

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
		if (Database.querySQLSet("INSERT INTO `social_posts` (`author`,`message`,`title`)"
				+ "VALUES" + "('" + inputAuthor + "','" + inputMessage + "','" + inputTitle + "');")) {
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
		if (Database.querySQLSet("INSERT INTO `social_posts`" + "(`author`," + "`message`," + "`title`," + "`parentPost`)"
				+ "VALUES" + "('" + inputAuthor + "','" + inputMessage + "','" + inputTitle + "','" + replyingToPostID + "');")) {
			return new Post(inputAuthor, inputMessage, inputTitle);
		} else {
			return null;
		}
	}
	
	/**
	 * This method returns a javascript object that has all the post's information.
	 * @return A JSON string that represents all the posts.
	 */
	public static String JSONAllPosts() {
		String postList = "{\"Posts\": [";
		
		try {
			try {
				Connection connection = Database.connect();
				String query = "SELECT * FROM social_posts WHERE parentPost IS NULL;";
				System.out.println("Executing Statement:\n\t" + query);
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);

				boolean first = true;
				
				while (results.next()) {
					if(first) {
						first = false;
					}else {
						postList += ",";
					}
					
					postList += new JSONStringer()
							.object()
								.key("ID").value(results.getInt("id"))
								.key("Title").value(results.getString("title"))
								.key("Author").value(results.getString("author"))
								.key("Reply").value("0")
							.endObject()
							.toString();
				}

				results.close();
				statement.close();
				Database.disconnect(connection);
				System.out.println("Execution Success");
			} catch (Exception e) {
				System.out.println("Query Error:\n\t" + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Login Error:\n\t" + e.getMessage());
		}
		postList += "]}"; //Close it.

		return postList;
	}

}