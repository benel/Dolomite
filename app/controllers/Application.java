package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import models.Sign;
import play.mvc.*;
import play.data.validation.Required;

public class Application extends Controller {

    public static void index() {
        render();
    }
	
	public static void inscription(
		/*
                @Required(message="The first password is required") String password1,
                @Required(message="The second password is required") String password2,
		@Required(message="The firstname is required") String firstname,
		@Required(message="The lastname is required") String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
                 */
                String password1,
                String password2,
		String firstname,
		String lastname,
		String login,
		String email,
		String signature) {

            Sign sign = new Sign();
            try {
                //if (Inscription.checksign(signature, firstname, lastname, email)) {
                if (sign.verifySig((firstname + lastname + email).getBytes(), signature, Sign.getPublicKey())) {
                    render();
                } else {
                    render("errors/error.html");
                }
            } catch (Exception ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }

            render();
	}
	
        public static void invitation(){
            render();
	}

		
}