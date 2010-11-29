package controllers;

import java.net.URLEncoder;
import java.util.*;
 
import play.*;
import play.mvc.*;
import play.libs.Crypto;
import play.libs.Mail;
import play.data.validation.*;
import java.io.UnsupportedEncodingException;

import notifiers.*;
import models.*;
import play.*;
import play.i18n.Messages;


/**
 * Invitation Controller
 * @method: inviteNewMember
 *
 * Other methods useless ...
 */

@With(Secure.class)
public class Invitation extends BaseController {

	@Before
	static void saveValuesIntoSession()
	{
		session.put("nom", params.get("nom"));
		session.put("prenom", params.get("prenom"));
		session.put("mail", params.get("mail"));
		session.put("langue", params.get("langue"));
	}

	/*
	 * inviteNewMember
	 *
	 */
	public static void inviteNewMember(@Required String nom,@Required String prenom, @Required String mail, @Required String langue) {
		String url = "";
		String signature = "";
		String community = "Hypertopic";
        
		try {
			url = "http://" + request.domain + ":" + Play.configuration.getProperty("site.port") + "/inscription?firstname=" + URLEncoder.encode(prenom, "UTF-8") + "&lastname=" + URLEncoder.encode(nom, "UTF-8") + "&email=" + URLEncoder.encode(mail, "UTF-8");
			signature = Crypto.sign(prenom + nom + mail);
			url += "&signature=" + signature;
			System.out.println(url);
		} catch (UnsupportedEncodingException uee) {
			System.err.println(uee);
		}
		if (validation.hasErrors()){
			render("Application/invitation.html");
		} else {
            if(renderArgs.get("domainName")!=null){
                community=renderArgs.get("domainName").toString();
            }
			if (langue.equals("fr")) {
				Mails.inviteFr("Hypertopic Team <noreply@hypertopic.org>", mail, prenom, nom, url, community);
			} else {
				Mails.inviteEn("Hypertopic Team <noreply@hypertopic.org>", mail, prenom, nom, url, community);
			}
			flash.success(Messages.get("invitation_success"));
            System.out.println(community);
			Application.invitation();
		}
	}
	
	public static void sendInvitation(
		String firstNameSender,
		String lastNameSender,
		@Required(message="First Name Receiver is required") String firstNameReceiver,
		@Required(message="Last Name Receiver is required") String lastNameReceiver,
		@Email @Required(message="Mail Receiver is required") String mailReceiver,
		@Required(message="Message Language is required")String msgLang
	){
		String signature ="hash";
		String message="";
		String sender="yessoufy@gmail.com";
		
		if (validation.hasErrors()){
				render("Application/invitation.html");
		}else{
			if(msgLang.equals("fr")){
				message = " Vous avez �t� invit�(e) par "+firstNameSender+" "+lastNameSender+"� rejoindre la communaut� hypertopic. Pour ce faire, veuillez vous inscrire en cliquant ici:";//Lien.signature;
			}else if(msgLang.equals("en")){
				message = "You have been invited by "+firstNameSender+" "+lastNameSender+" to register as a member to the Hypertopic community.";//Lien.signature;
			}
			//Mail.send(sender, "essoufy_@hotmail.com","sujet", message);
			flash.success(Messages.get("invitation_success"));
		}
		//show(); la vue pour afficher les erreur ou le succes de l'envoi d'invitation
	}
}
