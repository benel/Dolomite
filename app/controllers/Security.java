package controllers;
import javax.naming.NamingException;
import play.*;
import play.db.jpa.*;
import models.*;


public class Security extends Secure.Security{
	static boolean authenticate(String username, String password) {
        System.out.println("print: "+flash.get("url"));
         try{
		LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+ username +","+Play.configuration.getProperty("ldap.dn"), password);
		User user = (User)userConnection.retrieve(username);
		if (user!=null){
		return true;
		}else{
		return false;
		}
	
		}catch (NamingException e){
		 System.out.println("Exception dans Security.java: "+e.getMessage());
		 return false;
		}

	}

    static void onAuthenticated() {
        String url = session.get("url");
        session.remove("url");
        flash.put("url",url);
    }
}
