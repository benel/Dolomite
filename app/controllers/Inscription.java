package controllers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.logging.Level;
 
import play.mvc.*;
 
import play.data.validation.*;


import java.security.PublicKey;
import java.security.Signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Inscription extends Controller {
	 
	
	public static void adduser(
	
		@Required(message="The first password is required") String password1, 
                @Required(message="The second password is required") String password2,
		@Required(message="The firstname is required") String firstname,
		@Required(message="The lastname is required") String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
		String signature) {

            Inscription inscription = new Inscription();

            if (inscription.checksign(signature, firstname, lastname, email)) {
                
		if (validation.hasErrors()){
                    render("Application/inscription.html");
		} else {

                    if((password2.matches("[a-zA-Z0-9]+"))&&(password1.matches("[a-zA-Z0-9]+"))) {
                    
                        if((password2.matches("[a-zA-Z]+[0-9]+"))&&(password1.matches("[a-zA-Z]+[0-9]+"))) {

                            if(password2.equals(password1)) {

				//New entry in the active directory
				if ((!password1.equals(firstname))&&(!password1.equals(lastname))&&(!password1.equals(login))) {
					
                                    if ((password1.length()>=6)) {
					//new LdapUser(email, password1, firstname, lastname, login).addUser();
					flash.success("You have been successfully registered "+firstname+" "+lastname+". You can now get logged with your login and password.");
					render("Application/inscription.html");
                                    } else {
					flash.error("Your password should contain at least 6 characters");
					render("Application/inscription.html",firstname,lastname,email,signature);
                                    }

				} else {
                                    flash.error("Your password shouldn't be equal to your firstname, lastname or login");
                                    render("Application/inscription.html",firstname,lastname,email,signature);
				}

                            } else {
                                //Error message : passwords 1 and 2 dont match
                                flash.error("The passwords typed do not match");
                                render("Application/inscription.html",firstname,lastname,email,signature);
                            }

                        } else {
                            flash.error("Your password should contain at least five characters and a number");
                            render("Application/inscription.html",firstname,lastname,email,signature);
                        }
                    } else {
                        //Error message : passwords fields empty
                        flash.error("Your password shouldn't be empty");
                        render("Application/inscription.html",firstname,lastname,email,signature);
                     }
                }
            } else {
                System.out.println("signature erronée");
            }
        }


        /**
         * Checksign
         * Compare hash (firstname, lastname, email) to signature (in param)
         * @param signature
         * @param firstname
         * @param lastname
         * @param email
         * @return
         */
	protected boolean checksign(String signature, String firstname, String lastname, String email)
	{
            String data = firstname + lastname + email;
            Boolean isOk = false;

            try {
                Signature signer = Signature.getInstance("SHA1withDSA");
                signer.initVerify(Inscription.deserialize(".//.//public//public.Obj"));
                signer.update(data.getBytes());
                isOk =  signer.verify(signature.getBytes());

            } catch (NoSuchAlgorithmException ex) {
                java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SignatureException ex) {
                java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            }


            return isOk;
	}

 	 public static PublicKey deserialize( String file){

		 PublicKey key =null;
		 ObjectInputStream ois;
		try {

			ois = new ObjectInputStream(new FileInputStream(file));
			key  =(PublicKey)ois.readObject();
			ois.close();
			//System.out.println("key = "+key);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	 }
	
	public static void claim ()
	{
		//Mail sending to the sponsor
		//Mail.send("","","");
		//flash.success("Contactez votre parrain � l'adresse mentionn�e dans l'email que vous avez re�u");
	}

        public static void main () {
            Inscription.adduser(null, null, "qsdqsd", "qdsqd", "qsdqs", "qsdqs", "qsdqsd");
        }
	
}