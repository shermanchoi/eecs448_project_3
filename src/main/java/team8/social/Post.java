package team8.social;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
		inputMessage = StringEscapeUtils.escapeHtml4(inputMessage);
		inputTitle = StringEscapeUtils.escapeHtml4(inputTitle);

		String query = ("INSERT INTO `social_posts` (`author`,`message`,`title`)" + "VALUES" + "('" + inputAuthor
				+ "','" + inputMessage + "','" + inputTitle + "');");

		if (Database.querySQLSet(query)) {
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
		inputMessage = StringEscapeUtils.escapeHtml4(inputMessage);
		inputTitle = StringEscapeUtils.escapeHtml4(inputTitle);
		
		String query = ("INSERT INTO `social_posts`" + "(`author`," + "`message`," + "`title`," + "`parentPost`)"
				+ "VALUES" + "('" + inputAuthor + "','" + inputMessage + "','" + "Reply" + "','" + replyingToPostID
				+ "');");

		if (Database.querySQLSet(query)) {
			return new Post(inputAuthor, inputMessage, inputTitle);
		} else {
			return null;
		}
	}

	/**
	 * This method returns a javascript object that has all the post's information.
	 * 
	 * @return A JSON string that represents all the posts.
	 */
	public static String JSONAllPosts() {
		String query = "SELECT * FROM social_posts WHERE parentPost IS NULL;";
		DatabaseGetter getter = new DatabaseGetter(query);
		ResultSet rs = getter.results;
		
		int totalPosts = 0;
		JSONArray jsonArr = new JSONArray();
		
		try {			
			//Construct the post array.
			while (rs.next()) {
				totalPosts++;
				jsonArr.put(
						new JSONObject()
						.put("ID",rs.getInt("id"))
						.put("Title",rs.getString("title"))
						.put("Author",rs.getString("author"))
						.put("Reply",getParentCount(rs.getInt("id")))
						);
				
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}
		
		JSONStringer json = (JSONStringer) 
				new JSONStringer().object()
				.key("Posts").value(jsonArr)
				.key("currentP").value(1)
				.key("totalP").value(Math.max(1,totalPosts/10))
				.endObject();
		
		return json.toString();
	}
	/**
	 * This method returns a JS object that represents the posts that reply to a specific post given an id
	 * @param id 
	 *            The id of the parent post.
	 * @return A JSON String that contains the replies to a specific post given the id
	 */
	public static String JSONAllPostReplies(int id) {
		String query = "SELECT FROM social_posts WHERE parentPost='" + id + "'";
		DatabaseGetter getter = new DatabaseGetter(query);
		ResultSet rs = getter.results;
		
		int totalPosts = 0;
		JSONArray jsonArr = new JSONArray();
		
		try {
			//Just get all the replies of a post
			while (rs.next()) {
				totalPosts++;
				jsonArr.put(
						new JSONObject() //Start object
						.put("Author",rs.getString("author")) //Author of reply
						.put("Content",rs.getString("message")) //Content of reply
						); //End object
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}
		
		//Build the post object to return
		String postObject = new JSONStringer().object() //Start object
				.key("currentP").value(1) //Current page of the json object.
				.key("totalP").value(Math.max(1,totalPosts/10)) //The total pages of the json object
				.key("replies").value(jsonArr) //The replies to the object.
				.endObject().toString(); //End object
		
		return postObject;
	}
	/**
	 * This returns the number of replies a post has.
	 * 
	 * @param id
	 *            The id of the post
	 * @return The number of replies the post (associated with the id) has.
	 */
	private static int getParentCount(int id) {
		String query = "SELECT COUNT(*) FROM social_posts WHERE parentPost='" + id + "'";
		DatabaseGetter getter = new DatabaseGetter(query);
		ResultSet rs = getter.results;

		int count = 0;
		try {
			while (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return count;
	}

	/**
	 * This method returns a JSON representing a post using an id
	 * 
	 * @pre The post with the id exists.
	 * @param id_in
	 *            The id to input.
	 * @return JSON representing the post
	 */
	public static String getPostByID(int id_in) {
		String post = "";
		String query = "SELECT * FROM social_posts WHERE id=" + id_in + ";";
		DatabaseGetter getter = new DatabaseGetter(query);
		ResultSet rs = getter.results;

		
		try {
			while (rs.next()) {
				post = new JSONStringer().object()
						.key("ID").value(rs.getInt("id"))
						.key("Title").value(rs.getString("title"))
						.key("Author").value(rs.getString("author"))
						.key("Content").value(rs.getString("message"))
						.endObject().toString();
				
				System.out.println(rs.getString("message"));
			}
		} catch (Exception e) {
			System.out.println("ResultSet Error:\n\t" + e.getMessage());
		}

		return post;
	}
}