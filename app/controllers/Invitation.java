package controllers;
import play.libs.Mail;

import java.util.*;
 
import play.*;
import play.mvc.*;
 
import models.*;
import play.data.validation.*; 

public class Invitation extends Controller {

    	
	
	public static void sendInvitation( String firstNameSender,
								String lastNameSender,
								 @Required(message="First Name Receiver is required")String firstNameReceiver,
								 @Required(message="Last Name Receiver is required") String lastNameReceiver,
								 @Email @Required(message="Mail Receiver is required") String mailReceiver,								 						 
								 @Required(message="Message Language is required")String msgLang){
	
		String signature ="hash";
		String message="";
		//String sender="testSendInvitation@hypertopic-test.com";
		String sender="yessoufy@gmail.com";
		
		if (validation.hasErrors()){
				render("Application/show.html");
		}else{
			if(msgLang.equals("fr")){
				message = " Vous avez été invité(e) par "+firstNameSender+" "+lastNameSender+"à rejoindre la communauté hypertopic. Pour ce faire, veuillez vous inscrire en cliquant ici:";//Lien.signature;
			}else if(msgLang.equals("en")){
				message = "You have been invited by "+firstNameSender+" "+lastNameSender+" to register as a member to the Hypertopic community.";//Lien.signature;
			}
			
			Mail.send(sender, "essoufy_@hotmail.com","sujet", message);
			flash.success("Your invitation has been sent successfully!");
		}
		//show(); la vue pour afficher les erreur ou le succes de l'envoi d'invitation
	}
	
	
	
	
	
	
	
	
	


}