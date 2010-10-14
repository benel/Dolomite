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

public class Inscription extends Controller {

	public static void adduser(
		@Required(message="The first password is required") String password1,
                @Required(message="The second password is required") String password2,
		@Required(message="The firstname is required") String firstname,
		@Required(message="The lastname is required") String lastname,
		@Required(message="The login is required") String login,
		@Required(message="The email is required") String email,
		String signature) {

            if (signature.equals(Crypto.sign(firstname + lastname + email))) {
                if (validation.hasErrors()) {
                    render("Application/inscription.html");
                } else {
                    if ((password2.matches("[a-zA-Z0-9]+")) && (password1.matches("[a-zA-Z0-9]+"))) {
                        if ((password2.matches("[a-zA-Z]+[0-9]+")) && (password1.matches("[a-zA-Z]+[0-9]+"))) {
                            if (password2.equals(password1)) {
                                //New entry in the active directory
                                if ((!password1.equals(firstname)) && (!password1.equals(lastname)) && (!password1.equals(login))) {
                                    if (password1.length() >= 6) {
                                        new LdapUser(email, password1, firstname, lastname, login).addUser();
                                        flash.success("You have been successfully registered " + firstname + " " + lastname + ". You can now get logged with your login and password.");
                                        render("Application/inscription.html");
                                    } else {
                                        flash.error("Your password should contain at least 6 characters");
                                        render("Application/inscription.html", firstname, lastname, email, signature);
                                    }
                                } else {
                                    flash.error("Your password shouldn't be equal to your firstname, lastname or login");
                                    render("Application/inscription.html", firstname, lastname, email, signature);
                                }
                            } else {
                                //Error message : passwords 1 and 2 dont match
                                flash.error("The passwords typed do not match");
                                render("Application/inscription.html", firstname, lastname, email, signature);
                            }
                        } else {
                            flash.error("Your password should contain at least five characters and a number");
                            render("Application/inscription.html", firstname, lastname, email, signature);
                        }
                    } else {
                        //Error message : passwords fields empty
                        flash.error("Your password shouldn't be empty");
                        render("Application/inscription.html", firstname, lastname, email, signature);
                    }
                }
            } else {
                flash.error("signature erronée");
                render("Application/inscription.html");
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

            /*
            Boolean isOk = false;
            String data = firstname + lastname + email;
            System.out.println("data URL : " + data);
            System.out.println("signature URL : " + signature);
            System.out.println("signature URL getBytes : " + signature.getBytes());
            String newSignature;
            try {
            //Signature signer = Signature.getInstance("SHA1withDSA");
            //signer.initVerify(Inscription.deserialize("D:\\Documents\\Developpement\\play-1.0.1\\dolomite\\public\\public.Obj"));
            //signer.update(data.getBytes());
            newSignature = (Inscription.signData(data.getBytes(), Inscription.deserializePrivate("D:\\Documents\\Developpement\\play-1.0.1\\dolomite\\public\\private.Obj"))).toString();
            System.out.println("newSignature : " + newSignature);
            System.out.println("newSignature getBytes : " + newSignature.getBytes());
            //isOk = newSignature.verify(signature.getBytes());
            } catch (Exception ex) {
            Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            }
            //catch (NoSuchAlgorithmException ex) {
            //    java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (SignatureException ex) {
            //    java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (InvalidKeyException ex) {
            //    java.util.logging.Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            //}
            return isOk;
             */
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

	public static void claim () {
		//Mail sending to the sponsor
		//Mail.send("","","");
		//flash.success("Contactez votre parrain ? l'adresse mentionn?e dans l'email que vous avez re?u");
	}

}
