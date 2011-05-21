package notifiers;

import play.*;
import play.mvc.*;
import java.util.*;

public class Mails extends Mailer {

	public static void inviteFr (String from, String to, String firstname, String lastname, String url, String community, String firstNameGodfather, String lastNameGodfather, Object mailGodfather) {
		setSubject("Invitation a " + community);
		addRecipient(to);
		setFrom(from);
		setReplyTo(mailGodfather);
		send(firstname, lastname, url, community, firstNameGodfather, lastNameGodfather);
	}

	public static void inviteEn (String from, String to, String firstname, String lastname, String url, String community, String firstNameGodfather, String lastNameGodfather, Object mailGodfather) {
		setSubject("Invitation to " + community);
		addRecipient(to);
		setFrom(from);
		setReplyTo(mailGodfather);
		send(firstname, lastname, url, community, firstNameGodfather, lastNameGodfather);
	}
}
