package controllers;

import models.LdapUser;

public class Security extends Secure.Security{
	static boolean authenticate(String username, String password){
        System.out.println("print: "+flash.get("url"));
        
		return LdapUser.connect(username,password) != null;
		
	} 
    
    static void onAuthenticated() {
        String url = session.get("url");
        session.remove("url");
        flash.put("url",url); 
    }
}
