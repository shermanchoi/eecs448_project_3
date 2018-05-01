package team8.social.pages.admin;

import team8.social.PageHandler;
import team8.social.Post;
import team8.social.Session;
import team8.social.Admin;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.post;

public class AdminPage implements PageHandler{
    private static String admin = "";
    
    public AdminPage(){
        try{
        	admin = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/admin.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/admin.html");
        		admin = new String(IOUtils.toByteArray(i));
			} catch (Exception e2) {
				
			}
        	
        }
    }
    
    public void pages() {
        get("/admin", (req,res) ->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID")) || !Admin.isAdmin(req.session().attribute("UserID").toString())){
                res.redirect("/login");
                return null;
            }
            
            return admin;
        });
    
        post("/admin/deletePost", (req,res) ->{
            if(!Session.validate(req.session().id(),req.session().attribute("UserID")) || !Admin.isAdmin(req.session().attribute("UserID").toString())){
                res.redirect("/login");
                return null;
            }
            
            String postID = req.queryParams("ID");
            Admin.removePost(req.session().attribute("UserID").toString(), Integer.parseInt(postID));
            
            return admin;
        });
        
    }
}
