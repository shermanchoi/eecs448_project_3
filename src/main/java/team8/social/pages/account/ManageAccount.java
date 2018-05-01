package team8.social.pages.account;

import spark.utils.IOUtils;
import team8.social.Account;
import team8.social.Admin;
import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.get;
import static spark.Spark.post;
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
            InputStream i = getClass().getResourceAsStream("/public/html/myProfile.html");
            page = new String(IOUtils.toByteArray(i));
        } catch (Exception e) {}
    }
    
    public void pages() {
        get("/manageaccount", (req, res) -> {
            if (!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/");
            }
            
            return page;
        });
        post("/updatebio", (req,res) ->{
        	if (!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/");
            }
            
            String newBio = req.queryParams("changebioform");
            
            Account.changeBiography(req.session().attribute("UserID"),newBio);
            res.redirect("/manageaccount");
            return null;
        });
    }
}
