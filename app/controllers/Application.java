package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import models.Sign;
import play.mvc.*;
import play.data.validation.Required;
import play.libs.Crypto;
import models.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import play.*;
import play.i18n.Messages;

public class Application extends BaseController {

	public static void index() {
        render();
       
	}

	public static void inscription(
        String password1,
        String password2,
		String firstname,
		String lastname,
		String login,
		String email,
		String signature) {
        
        try{            
            if (signature.equals(Crypto.sign(firstname + lastname + email))) {
                login = normalize(firstname)+'.'+normalize(lastname);
                params.put("login",login);
                if (userExists(login)){
                    render("Application/reset.html",login);
                }
                else{
                    render();
                }
            } else {
                render("errors/error.html");
            }
        }catch(Exception e) {
            System.err.println(e);
            render("Application/index.html"); 
        }
	}

     
    
}
