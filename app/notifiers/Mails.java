package notifiers;

import java.util.Map;
import java.net.URLEncoder;
import play.*;
import play.mvc.*;
import play.i18n.Messages;
import play.libs.Crypto;
import models.Ldap;

// TODO Move URL, fullName and address generation into User class

public class Mails extends Mailer {

  static String encode(String s) throws Exception {
    return URLEncoder.encode(s, "UTF-8");
  }

  public static void invite(
    String community,
    String firstName,
    String lastName,
    String eMail,
    String sponsorLogin,
    String server,
    int port
  ) throws Exception {
    String contentPrologue = "";
    if (!"admin".equals(sponsorLogin)) {
      Map <String, String> sponsor = Ldap.getConnectedUserInfos(sponsorLogin);
      String sponsorFullName = sponsor.get("firstName") + " " 
        + sponsor.get("lastName"); 
      setReplyTo(sponsorFullName + " <" + sponsor.get("mail") +">");
      contentPrologue += Messages.get("invitation_prologue", sponsorFullName, community); 
    }
    setFrom(community + " <noreply@" + server + ">");
    setSubject(Messages.get("invitation_subject", community));
    String recipient = firstName + " " + lastName + " <" + eMail + ">";
    addRecipient(recipient);
    String url = "http://" + server
      + ((port==80)? "" : ":" + port) 
      + "/registration?firstname=" + encode(firstName)
      + "&lastname=" + encode(lastName)
      + "&email=" + encode(eMail)
      + "&signature=" + Crypto.sign(firstName + lastName + eMail);
    String contentPassword = Messages.get("invitation_password", url);
    String contentKeep = Messages.get("invitation_keep");
    send(contentPrologue, contentPassword, contentKeep);
  }
 
}
