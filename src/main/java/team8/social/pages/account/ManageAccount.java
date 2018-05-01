package team8.social.pages.account;

import team8.social.PageHandler;
import team8.social.Session;

import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;
import static spark.Spark.redirect;

/*****
 * Object: Account
 * Description: 
 * Author: William Graham
 * Modified: April 27, 2018
 *****/

public class ManageAccount implements PageHandler {
    private static String page = "";
    
    public ManageAccount() {
        try {
            page = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/createPost.html").toURI())
            ));
        } catch (Exception e) {}
    }
    
    public void pages() {
        get("/account", (req, res) -> {
            if (!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/");
            }
            
            return page;
        });
    }
}
