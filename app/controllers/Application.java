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
            try {
                firstname = URLDecoder.decode(firstname, "UTF-8");
                lastname = URLDecoder.decode(lastname, "UTF-8");
                email = URLDecoder.decode(email, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                System.err.println(uee);
                render("Application/index.html");
            }
            
            if (signature.equals(Crypto.sign(firstname + lastname + email))) {
                login = firstname+'.'+lastname;
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

        public static void invitation(){

		params.put("nom", session.get("nom") );
		params.put("prenom", session.get("prenom") );
		params.put("mail", session.get("mail") );
		//params.put("langue", session.get("langue") );
        
		String lang = session.get("langue");
		if(lang != null) {
			params.put("checked_fr", "");
			params.put("checked_en", "");

			if(lang.equals("fr"))
			{
				params.put("checked_fr", "checked");
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
			else if(lang.equals("en"))
			{
				params.put("checked_en", "checked");
			}
			else
			{
				System.out.println("Language error! Please check controller Invitation");
			}
		}                    
		render();
	}
    
}
