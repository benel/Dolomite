package controllers;

import java.util.*;
 
import play.*;
import play.mvc.*;
 
import models.*;
import play.data.validation.*;
import play.libs.*;

import play.db.jpa.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;

public class Inscription extends Controller {
	 
	
	public static void adduser(
	
		@Required(message="The first password is required") String password1, 
        @Required(message="The second password is required") String password2,
		@Required(message="The firstname is required") String firstname,
		@Required(message="The lastname is required") String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
		String signature)
	{
		if (validation.hasErrors()){
				render("Application/inscription.html");
		}
		else{
		if((password2.matches("[a-zA-Z0-9]+"))&&(password1.matches("[a-zA-Z0-9]+")))
		{
		if((password2.matches("[a-zA-Z]+[0-9]+"))&&(password1.matches("[a-zA-Z]+[0-9]+")))
		{
			if(password2.equals(password1))
			{
				//New entry in the active directory
				if ((!password1.equals(firstname))&&(!password1.equals(lastname))&&(!password1.equals(login)))
				{
					if ((password1.length()>=6))
					{
						//new LdapUser(email, password1, firstname, lastname, login).addUser();
						flash.success("You have been successfully registered "+firstname+" "+lastname+". You can now get logged with your login and password.");
						render("Application/inscription.html");
					}
					else
					{
						flash.error("Your password should contain at least 6 characters");
						render("Application/inscription.html",firstname,lastname,email,signature);
					}
				}
				else
				{
					flash.error("Your password shouldn't be equal to your firstname, lastname or login");
					render("Application/inscription.html",firstname,lastname,email,signature);
				}
			}
			else
			{
				//Error message : passwords 1 and 2 dont match
				flash.error("The passwords typed do not match");
				render("Application/inscription.html",firstname,lastname,email,signature);
			}
		}
		else
		{
			flash.error("Your password should contain at least five characters and a number");
			render("Application/inscription.html",firstname,lastname,email,signature);
		}
		}
		else
		{
			//Error message : passwords fields empty
			flash.error("Your password shouldn't be empty");
			render("Application/inscription.html",firstname,lastname,email,signature);
		}
		}
	}
	
	public boolean checksign(String signature)
	{
		//Check the key
		return true;
	}
	
	public static void claim ()
	{
		//Mail sending to the sponsor
		//Mail.send("","","");
		//flash.success("Contactez votre parrain à l'adresse mentionnée dans l'email que vous avez reçu");
	}
	
}