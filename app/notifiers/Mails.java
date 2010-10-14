package notifiers;

import play.*;
import play.mvc.*;
import java.util.*;

public class Mails extends Mailer {

	public static void inviteFr (String from, String to, String firstname, String lastname, String url) {
		setSubject("Invitation a Hypertopic");
		addRecipient(to);
		setFrom(from);
		send(firstname, lastname, url);
	}

	public static void inviteEn (String from, String to, String firstname, String lastname, String url) {
		setSubject("Invitation to Dolomite");
		addRecipient(to);
		setFrom(from);
		send(firstname, lastname, url);
	}
}
