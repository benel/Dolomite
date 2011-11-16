package controllers;

import java.io.*;
import java.util.logging.*;
import models.LdapUser;
import play.mvc.*;
import play.libs.Crypto;
import play.data.validation.*;
import play.*;
import play.i18n.Messages;

public class Registration extends BaseController {

	static void check(
		String password1,
		String password2,
		String firstname,
		String lastname,
		String email,
		String signature
  ) throws LocalizedException {
    if (!signature.equals(Crypto.sign(firstname+lastname+email)))
      throw new LocalizedException("msg_signature_no_match");
		if (password1==null || password1.length()<6) 
			throw new LocalizedException("error_short_pass_msg");
		if (!password1.equals(password2)) 
			throw new LocalizedException("error_pass_no_match_msg");
		if (!password1.matches(".*[a-zA-Z].*")) 
			throw new LocalizedException("error_alpha_pass_msg");
		if (!password1.matches(".*[0-9].*")) 
			throw new LocalizedException("error_num_pass_msg");
	}

	public static void index(
		String password1,
		String password2,
		@Required String firstname,
		@Required String lastname,
		@Required String email,
		@Required String signature
  ) throws LocalizedException {
    if (!validation.hasErrors()) {
      check(password1, password2, firstname, lastname, email, signature);
      String login = normalize(firstname, lastname);
      LdapUser user = new LdapUser(email, password1, firstname, lastname, login);
      if (userExists(login)) {
        user.updateUser(email, password1, firstname, lastname);
      } else {
        user.addUser();
      }
      flash.now("success", Messages.get("registration_success", login));
    }
    render();
	}
  
  @Catch(LocalizedException.class)
  public static void displayError(LocalizedException e) {
      flash.now("error", e.getMessage());
      render();
  }

}
