/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Administrateur
 */
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.AuthenticationException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import play.*;

public class LDAPDirectory {

    Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
    String ldap_account;
    DirContext ldapContext = null;

 	public  LDAPDirectory (String ldap_server, String ldap_account, String ldap_password)throws NamingException
	{
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldap://" + ldap_server);	// localhost:389
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");		// simple, none, list of SASL mechanisms
        ldapEnv.put(Context.SECURITY_PRINCIPAL, ldap_account);			// cn=admin,dc=placeoweb
        ldapEnv.put(Context.SECURITY_CREDENTIALS, ldap_password);		// motdepasse
        this.ldap_account = ldap_account;
        this.ldapContext = new InitialDirContext(ldapEnv);
	}

	public void create (Entry entry) throws NamingException{

			
            BasicAttribute cm= (BasicAttribute)entry.toLDAPAttributes().get("cn");
            String cn= (String)cm.get();
            if (retrieve(cn)== null)
            {    
            ldapContext.bind("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"), null, entry.toLDAPAttributes());
            }
            
			
    }

    public Entry retrieve(String cn) throws NamingException{
        Attributes attr = null;    
        try {
            SearchControls controle = new SearchControls();
            controle.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String critere = "(cn=" + cn + ")"; // ok					ldapsearch -x -h localhost -b 'dc=placeoweb' '(cn=*)'
            try {
            NamingEnumeration<SearchResult> e = ldapContext.search(Play.configuration.getProperty("ldap.dn"), critere, controle);
            
            while (e.hasMore()) {
            SearchResult r = e.next();
            attr=r.getAttributes();

            }

            } catch (NoInitialContextException y) {
            	return null;}
            
            
				// on est certain d'avoir un uniq result
                //System.out.println("name: " + r.getName());
                //System.out.println("object: " + r.getClassName());
                //System.out.println("getAttributes: " + r.getAttributes());

			   //System.out.println("Cn vaut [[["+attr.get("userPassword").get()+ "]]]]");

                if(attr.get("owner")!=null){// il s'agit d'un group

		ArrayList<String> members= new ArrayList();
		String groupName=(String)attr.get("cn").get();
		String[] membersLDAP = attr.get("member").toString().split(",");
		for (int i=0; i < membersLDAP.length; i++){
			String var = membersLDAP[i];
			String[] varMembers = var.split("=");
			
			if (varMembers[0].contains("cn"))
			{
				members.add(varMembers[1]);
			}
			}
                    String owner=(String)attr.get("owner").get();
                    String[] ownerList = owner.split(",");
                    String[] ownerCN = ownerList[0].split("=");
                    String ownerName = ownerCN[1];
                    
		Entry grp=new Group(groupName,members,ownerName);
                    
                    return grp;

                }else if (attr.get("mail")!=null){
				
				   String email=(String)attr.get("mail").get();
                   String firstname=(String)attr.get("givenName").get();
                   String lastname=(String)attr.get("sn").get();
                   String  login=(String)attr.get("cn").get();
				   Entry usr=new User(email, firstname, lastname, login);

                   return usr;



                } else if (attr.get("cn").get().equals("admin")){// c'est l'admin
                     String  login=(String)attr.get("cn").get();
				     Entry user= new User(Play.configuration.getProperty("ldap.admin.password"),login);
					 return user;
				}
				else{
				return null;
				
				}
            } catch (NullPointerException y){return null;}
		

            }




    public void update(Entry entry)throws NamingException{
            BasicAttribute cm= (BasicAttribute)entry.toLDAPAttributes().get("cn");
            String cn= (String)cm.get();
            ldapContext.modifyAttributes("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"),
            DirContext.REPLACE_ATTRIBUTE, entry.toLDAPAttributes());


    }

    public void delete(Entry entry) throws NamingException{
            BasicAttribute cm= (BasicAttribute)entry.toLDAPAttributes().get("cn");
            String cn= (String)cm.get();
            String delete = "cn="+cn+","+Play.configuration.getProperty("ldap.dn");
            ldapContext.unbind(delete);
    }
    
    public void closeConnection() throws NamingException{
    	ldapContext.close();
    	}
}