package controllers;

import play.*;
import play.mvc.*;
import play.libs.Mail;
import play.data.validation.*;
import play.i18n.Messages;

import notifiers.Mails;
import models.Ldap;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;

@With(Secure.class)
public class Invitation extends BaseController {

	public static void index(
    @Required String firstName,
    @Required String lastName,
    @Required String eMail
  ) throws Exception {
    if (!validation.hasErrors()) {
      if (!isMalicious(firstName, lastName, eMail)) {
        String community = (renderArgs.get("domainName")==null) //TODO ?
          ? "Hypertopic"
          : renderArgs.get("domainName").toString();
        Mails.invite(
          community, firstName, lastName, eMail,
          session.get("username"), request.domain, request.port
        );
        flash.success(Messages.get("invitation_success"));
        Invitation.index("", "", ""); //TODO other way to clear parameters?
      } else {
        flash.error(Messages.get("invitation_mailadresse_no_match"));
      }
    }
    render();
 	}

  //TODO LDAP search refactoring
  static boolean isMalicious(String firstName, String lastName, String eMail) throws Exception {
    String previousMail = null;
    Ldap ldap = new Ldap();
    ldap.SetEnv(
      Play.configuration.getProperty("ldap.host"),
      Play.configuration.getProperty("ldap.admin.dn"), 
      Play.configuration.getProperty("ldap.admin.password")
    );
    Attributes user = ldap.getUserInfo(ldap.getLdapEnv(), normalize(firstName, lastName));
    if (user!=null) {
      NamingEnumeration e = user.getAll();
      while(previousMail==null && e.hasMore()) {
        Attribute a = (Attribute) e.next();
        if ("mail".equals(a.getID())) {
          previousMail = a.get().toString();
        }
      }
    }
    return (previousMail!=null && !previousMail.equals(eMail));
  }
	
}
