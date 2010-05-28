package controllers;

import models.LdapUser;
 
public class Security extends Secure.Security {
    
    static boolean authenticate(String username, String password) {
        return LdapUser.connect(username,password) != null ;
    }    
    
}
