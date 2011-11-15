package controllers;

import java.util.logging.*;
import play.mvc.*;
import play.data.validation.Required;
import play.libs.Crypto;
import models.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import play.*;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class BaseController extends Controller {

    @Before
    static void getDomain(){
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
    
    public static String normalize(String original){
        // to lower case
        String str = original.toLowerCase();  
        
        // replace multiple spaces with one space
        str = str.replaceAll(" +"," ");
        
        // drop initial or final spaces        
        str = str.trim();
        
        // normalize and remove accents (diacritics)
        str = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","");
                
        // replace some separators with underscore
        str = str.replaceAll("[- .']","_");
        
        // keep only alphanumeric characters and underscores
        str = str.replaceAll("[^(_|a-z|0-9)]","");
        return str;
    }
}       
