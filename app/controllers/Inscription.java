package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Sign;
import models.LdapUser;
import play.mvc.*;
import play.libs.Crypto;
import play.data.validation.*;
import play.*;
import play.i18n.Messages;


public class Inscription extends BaseController {

	public static void adduser(
		@Required(message="The first password is required") String password1,
		@Required(message="The second password is required") String password2,
		@Required(message="The firstname is required") String firstname,
		@Required(message="The lastname is required") String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
		String signature) {
		System.out.println('1');
		int result = -1;
        String checkResult;
		try {
			if (signature.equals(Crypto.sign(firstname + lastname + email))) {
				if (validation.hasErrors()) {
					render("Application/inscription.html");
				} else {
                    checkResult = checkPass(password1,password2,firstname,lastname,login);
                    if ( checkResult == null){ //valid passwords
                        result = new LdapUser(email, password1, firstname, lastname, login).addUser();
                        System.out.println(result);
                        if (result==0) {
                            //user doesn't exist yet
                            flash.now("success","You have been successfully registered " + firstname + " " + lastname + "." );
                        } else if(result==1) {
                            //user already exists
                            flash.now("success","You have already been successfully registered " + firstname + " " + lastname + "." );
                        }
                        render("Application/inscription.html");
					}
					else{
						flash.now("error",checkResult);
                        render("Application/inscription.html", firstname, lastname, email, signature);
					}
                }
			} else {
				flash.now("error",Messages.get("msg_signature_no_match"));
				render("Application/inscription.html");
			}
		} catch (Exception e) {
			System.out.println("An exception occurred in Inscription.adduser()");
			e.printStackTrace();
			render("Application/index.html");
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
	public static boolean checksign(String signature, String firstname, String lastname, String email) {
			Boolean result = false;

		try {
			String data = firstname + lastname + email;
			byte[] dataByte = data.getBytes();
			result = Inscription.verifySig(dataByte, signature.getBytes(), Inscription.deserializePublic(System.getProperty("user.dir") + "/conf/key.pub"));
			   System.out.println("check sign : " + result);
		} catch (Exception ex) {
			Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}


	 public static boolean verifySig(byte[] data, byte[] signData, PublicKey pubKey) throws Exception {
			Signature signer = Signature.getInstance("SHA1withDSA");
			signer.initVerify(pubKey);
			signer.update(data);
					//System.out.println("signer : " + signer.toString());
			return (signer.verify(signData));

	}

	 public static byte[] signData(byte[] data, PrivateKey privKey) throws Exception {

		 Signature signer = Signature.getInstance("SHA1withDSA");
		 signer.initSign(privKey);
		 signer.update(data);

		 return (signer.sign());
	}

 	 public static PublicKey deserializePublic(String file) {

				PublicKey key =null;
				ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			key = (PublicKey)ois.readObject();
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

	public static PrivateKey deserializePrivate(String file){

		 PrivateKey key = null;
		 ObjectInputStream ois;
		try {

			ois = new ObjectInputStream(new FileInputStream(file));
			key  =(PrivateKey)ois.readObject();
			ois.close();
			System.out.println("key = "+key);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return key;
	 }


	public static void resetPass (
		@Required(message="The first password is required") String password1,
		@Required(message="The second password is required") String password2,
		@Required String firstname,
		@Required String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
		@Required(message="Invalid signature") String signature) {

		String checkResult;
		try {
			if (signature.equals(Crypto.sign(firstname + lastname + email))) {
				if (validation.hasErrors()) {
					render("Application/reset.html");
				} else {
					checkResult = checkPass(password1,password2,firstname,lastname,login);
					System.out.println("v: "+validation);
					if ( checkResult == null){ //valid passwords
						// retrieve the user and change the password
						new LdapUser(email, password1, firstname, lastname, login).updateUser(email,password1,firstname,lastname);
						flash.now("success","Your password has been successfully changed");
						System.out.println("params: "+params.toString());
						params.remove(signature);
					}
					else{
						flash.now("error",checkResult);
					}
				}
			}
			render("Application/reset.html");
		} catch (Exception e)
		{
			render("Application/index.html");
		}

	}

	public static String checkPass(String password1, String password2, String firstname, String lastname, String login) {
		if (password1.length() < 6) 
			return Messages.get("error_short_pass_msg");
		if (!password2.equals(password1)) {
			return Messages.get("error_pass_no_match_msg");
		if (!password1.matches("[a-zA-Z]") || !password1.matches("[0-9]")) {
			return Messages.get("error_alfanum_pass_msg");
		return null;
	}

}



