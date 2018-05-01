/*****
 * Object: Main
 * Description: 
 * Author: William Graham
 * Modified: April 04, 2018
 *****/
package team8.social;

import team8.social.pages.*;
import team8.social.pages.account.*;
import team8.social.pages.post.*;

import java.util.Vector;


import static spark.Spark.*;

public class Main {
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.print("Social Server Started\n");
			if(configure()) {
				// define pages
				pages();
			}else {
				System.out.println("Failed to connect to database!");
				System.out.println("The default server is down.");
			}
		}else if(args.length == 3) {
			System.out.print("Social Server Started\n");
			if(configure(args[0],args[1],args[2])) {
				// define pages
				pages();
			}else {
				System.out.println("Failed to connect to database!");
				System.out.println("Did you type in the correct information?");
			}
		}
	}

	public static boolean configure() {
		port(80);
		// ("jdbc:mysql://localhost:3306/sys","root","password")
		staticFiles.location("/public");
		staticFiles.externalLocation("./resources");
		return Database.initialize("jdbc:mysql://mysql.eecs.ku.edu/w751g500", "w751g500", "eig4Jaix");
	}
	
	public static boolean configure(String url, String username, String password) {
		port(80);
		// ("jdbc:mysql://localhost:3306/sys","root","password")
		staticFiles.location("/public");
		staticFiles.externalLocation("./resources");
		return Database.initialize(url, username, password);
	}

	/**
	 * Defines how URIs are handled.
	 * 
	 * @post Server knows how to handle a number of URIs
	 */
	public static void pages() {
		Vector<PageHandler> pages = new Vector<PageHandler>();

		pages.add(new Root());

		// All pages that pertain to accounts
		pages.add(new CreateAccount());
		pages.add(new ForgotPassword());
		pages.add(new Login());
		pages.add(new Logout());
		pages.add(new ManageAccount());

		// All pages that pertain to posts
		pages.add(new ViewPost());
		pages.add(new CreatePost());
		pages.add(new ReplyPost());

		// All pages that pertain to administration

		// Registers all of the pages
		for (Integer i = 0; i < pages.size(); i += 1) {
			pages.get(i).pages();
		}

		

		get("/api/posts", (req, res) -> {
			return Post.JSONAllPosts();
		});

		get("/api/post", (req, res) -> {
			return Post.getPostByID(Integer.parseInt(req.queryParams("postID")));
		});

		get("/api/postReply", (req, res) -> {
			return Post.JSONAllPostReplies(Integer.parseInt(req.queryParams("postID")));
		});

		get("/api/security", (req, res) -> {
			return Account.getSecurityQuestions(req.session().attribute("forgot-username"));
		});
	}
}
