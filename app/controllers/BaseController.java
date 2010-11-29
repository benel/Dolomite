package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import models.Sign;
import play.mvc.*;
import play.data.validation.Required;
import play.libs.Crypto;
import models.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import play.*;


public class BaseController extends Controller {
   
    @Before
    public static void getDomain(){
        String domain = request.domain;
        String domainName = Play.configuration.getProperty(domain + ".name");
        String domainHref = Play.configuration.getProperty(domain + ".href");
        System.out.println("domain name: " + domainName);
        System.out.println("domain Href: " + domainHref);

        renderArgs.put("domainName", domainName);
        renderArgs.put("domainHref", domainHref);
       
    }
    
    
    public static boolean userExists(String login) {

        //verify that the user doesn't exist yet
        Ldap adminConnection = new Ldap();
        adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
        if(adminConnection.getUserInfo(adminConnection.getLdapEnv(),login)!=null){return true;}
        else{return false;}
    
    }
}
