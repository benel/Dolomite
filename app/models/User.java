package models;

import java.util.*;
import javax.persistence.*;


import play.*;
import play.db.jpa.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;

import controllers.*;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;


public class User extends Entry{

	private String email;
	private String password;
	private String login;
	private String firstname;
	private String lastname;

	public User (String email, String password, String firstname, String lastname, String login) {
	this.email = email;
	this.password = password;
	this.firstname=firstname;
	this.lastname=lastname;
	this.login = login;

	}
	
	public User (String email, String firstname, String lastname, String login) {
	this.email = email;
	this.firstname=firstname;
	this.lastname=lastname;
	this.login = login;

	}
	
	public User(String password, String login){
	this.password=password;
	this.login=login;
	}
	
	public String getEmail(){return email;}
	public String getLogin(){return login;}
	public String getPassword(){return password;}
	public String getFirstname(){return firstname;}
	public String getLastname(){return lastname;}
	
	public void setEmail (String email){this.email = email;}
	public void setLogin (String login){this.login = login;}
	public void setPassword (String password){this.password = password;}
	public void setFirstname (String firstname){this.firstname = firstname;}
	public void setLastname (String lastname){this.lastname = lastname;}

    @Override
    public BasicAttributes toLDAPAttributes() {

         Attributes attributes = new BasicAttributes(true);
        attributes.put(new BasicAttribute("mail", email));
        attributes.put(new BasicAttribute("objectClass", "inetOrgPerson"));
        // attributes.put(new BasicAttribute("objectClass","simpleSecurityObject"));
        attributes.put(new BasicAttribute("givenName", firstname));
        attributes.put(new BasicAttribute("sn", lastname));
        attributes.put(new BasicAttribute("cn", login));
        attributes.put(new BasicAttribute("userPassword",password));

        return (BasicAttributes)attributes;
    }

   

}
