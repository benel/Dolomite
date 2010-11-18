package notifiers;

import play.*;
import play.mvc.*;
import java.util.*;

public class Mails extends Mailer {

	public static void inviteFr (String from, String to, String firstname, String lastname, String url, String community) {
		setSubject("Invitation a " + community);
		addRecipient(to);
		setFrom(from);
		send(firstname, lastname, url, community);
	}

	public static void inviteEn (String from, String to, String firstname, String lastname, String url, String community) {
		setSubject("Invitation to " + community);
		addRecipient(to);
		setFrom(from);
		send(firstname, lastname, url, community);
	}
}
