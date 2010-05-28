package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;

import controllers.*;

 
public class LdapUser {
 
    public String email;
    public String password;
	public String login;
	public String firstname;
	public String lastname;
    //public boolean isAdmin;
	
	Ldap userConnection;
    
    public LdapUser(String email, String password, String firstname, String lastname, String login) {
        this.email = email;
        this.password = password;
		this.firstname=firstname;
		this.lastname=lastname;
		this.login = login;
		
		userConnection = new Ldap();
		userConnection.SetEnv("test.hypertopic.org:389","cn="+login+",dc=hypertopic,dc=org", password);
    }
	
	public static LdapUser connect(String login, String password) {
		//return find("byLoginAndPassword", login, password).first();
		
		
		Ldap staticConnection = new Ldap();
		staticConnection.SetEnv("test.hypertopic.org:389","cn="+login+",dc=hypertopic,dc=org", password);
		
		LdapUser newUser;
		
		String email="";
		String firstname="";
		String lastname="";
			

		Attributes atts=staticConnection.getUserInfo(staticConnection.getLdapEnv(), login);
		
		if(atts==null)
		{
			return null;
		}
		
		try {
			for (NamingEnumeration e = atts.getAll(); e.hasMore();) 
			{
				Attribute a = (Attribute) e.next();
				//System.out.println(a.getID() + ":");
				
				
				
				String attributeName = a.getID();
				String attributeValue="";
				
				Enumeration values = a.getAll();
				while (values.hasMoreElements()) {
					//System.out.println("valeur : " + values.nextElement().toString());
					attributeValue = values.nextElement().toString();
					
					System.out.println("value " + attributeValue.toString());
				}
				
				
				if(attributeName.equals("mail"))
				{
					email = attributeValue;
				}
				else if(attributeName.equals("givenName"))
				{
					firstname=attributeValue;
				}
				else if(attributeName.equals("sn"))
				{
					lastname = attributeValue;
				}	
				
			}
			
			newUser = new LdapUser(email, password, firstname, lastname, login);
			
			return newUser;
			
		} catch (javax.naming.NamingException e) {
			System.out.println(e.getMessage());
		}
		
		return null;

	}

	public void addUser() {
	//	this.save();
		Ldap adminConnection = new Ldap();
		adminConnection.SetEnv("test.hypertopic.org:389","cn=admin,dc=hypertopic,dc=org" ,"if052010");
		
		adminConnection.addUser(adminConnection.getLdapEnv(), email, firstname, lastname, login, password);
	}

	public void updateUser(String email, String password, String firstname, String lastname){
		this.email = email;
		this.password = password;
		//this.refresh();
		
		Ldap adminConnection = new Ldap();
		adminConnection.SetEnv("test.hypertopic.org:389","cn=admin,dc=hypertopic,dc=org" ,"if052010");
		
		
		adminConnection.modifyAttribute(adminConnection.getLdapEnv(), this.getLogin(), "givenName", firstname);
		adminConnection.modifyAttribute(adminConnection.getLdapEnv(), this.getLogin(), "sn", lastname);
		adminConnection.modifyAttribute(adminConnection.getLdapEnv(), this.getLogin(), "mail", email);
		adminConnection.modifyAttribute(adminConnection.getLdapEnv(), this.getLogin(), "userPassword", password);
	}
	
	public void deleteUser (){
	//	this.delete();
		Ldap adminConnection = new Ldap();
		adminConnection.SetEnv("test.hypertopic.org:389","cn=admin,dc=hypertopic,dc=org" ,"if052010");
		adminConnection.deleteUser(adminConnection.getLdapEnv(), "cn="+login+",dc=hypertopic,dc=org");
	}
	
	public String getEmail(){return email;}
	public String getLogin(){return login;}
	public String getFirstname(){return firstname;}
	public String getLastname(){return lastname;}

}	
public void listgroup (String member) {
	try {
		Hashtable<String,String> env = new Hashtable<String,String>();
		 
		// recherche en profondeur
		SearchControls controle = new SearchControls();
		controle.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String critere = "(|(member="+member+")(objectclass=groupOfNames*))";
		DirContext ictx = new InitialDirContext(env);
		NamingEnumeration<SearchResult> e = ictx.search("", critere, controle);
		while (e.hasMore()) {
		SearchResult r = e.next();
		System.out.println("name: " + r.getName());
	   // System.out.println("object: " + r.getClassName());
		listerAttributs(r.getAttributes());

			}
			}
			catch (javax.naming.NamingException e) {
			System.err.println("Exception " + e);
			}
			}
