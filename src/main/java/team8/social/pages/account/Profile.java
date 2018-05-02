package team8.social.pages.account;

import spark.utils.IOUtils;
import team8.social.PageHandler;
import team8.social.Session;

import java.io.InputStream;

import static spark.Spark.get;

/*****
 * Object: Profile
 * Description: 
 * Author: William Graham
 * Modified: May 02, 2018
 *****/

public class Profile implements PageHandler{
    private static String page;
    
    public Profile(){
        //Grab content of Security Questions page
        try{
            InputStream i = getClass().getResourceAsStream("/public/html/profile.html");
            page = new String(IOUtils.toByteArray(i));
        }catch(Exception e){}
    }
    
    public void pages(){
        get("/profile", (req,res)->{
            if(!Session.validate(req.session().id(), req.session().attribute("UserID"))) {
                res.redirect("/login");
                return null;
            }
            
            return page;
        });
    }
}
