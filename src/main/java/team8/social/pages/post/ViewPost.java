package team8.social.pages.post;

import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.utils.IOUtils;

import static spark.Spark.get;

public class ViewPost implements PageHandler {
    private static String view = "";
    private static String reply = "";
    
    public ViewPost(){
        try{
            view = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/postView.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/postView.html");
        		view = new String(IOUtils.toByteArray(i));
			} catch (Exception e2) {
				
			}
        }
    
        try{
            reply = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/public/html/postViewReply.html").toURI())
            ));
        }catch(Exception e){
        	try {
        		InputStream i = getClass().getResourceAsStream("/public/html/postViewReply.html");
        		reply = new String(IOUtils.toByteArray(i));
			} catch (Exception e2) {
				
			}
        }
    }
    
    public void pages() {
        get("/post", (req, res)->{
            if (!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/login");
                return null;
            }
            
            return view;
        });
    }
}
