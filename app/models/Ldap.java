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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
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

public class Ldap {

    /**
     * @param args
     */
    Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
    String ldap_account;

    public static void listerAttributs(Attributes atts) {
        try {
            for (NamingEnumeration e = atts.getAll(); e.hasMore();) {
                Attribute a = (Attribute) e.next();
                System.out.println(a.getID() + ":");
                Enumeration values = a.getAll();
                while (values.hasMoreElements()) {
                    System.out.println("valeur : " + values.nextElement().toString());
                }
            }
        } catch (javax.naming.NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(Hashtable ldapEnv, String mail, String givenName, String sn, String cn, String userPassword) {

        Attributes attributes = new BasicAttributes(true);
        attributes.put(new BasicAttribute("mail", mail));
        attributes.put(new BasicAttribute("objectClass", "inetOrgPerson"));
        // attributes.put(new BasicAttribute("objectClass","simpleSecurityObject"));
        attributes.put(new BasicAttribute("givenName", givenName));
        attributes.put(new BasicAttribute("sn", sn));
        attributes.put(new BasicAttribute("cn", cn));
        attributes.put(new BasicAttribute("userPassword", userPassword));

        DirContext ldapContext = null;
        try {
            ldapContext = new InitialDirContext(ldapEnv);
            // MonObjet objet = new MonObjet("valeur1","valeur2");
            ldapContext.bind("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"), null, attributes);
            ldapContext.close();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'acces au serveur LDAP " + e);
            e.printStackTrace();
        }
        System.out.println("fin des traitements");
    }

    public void deleteUser(Hashtable ldapEnv, String account) {
        DirContext ldapContext = null;
        try {
            ldapContext = new InitialDirContext(ldapEnv);
            ldapContext.unbind(account);
            ldapContext.close();
        } catch (NamingException e) {
            System.err.println("Erreur lors de l'acces au serveur LDAP" + e);
            e.printStackTrace();
        }
        System.out.println("fin des traitements");
    }

    public void SetEnv(String ldap_server, String ldap_account, String ldap_password) {


        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldap://" + ldap_server);	// localhost:389
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");		// simple, none, list of SASL mechanisms
        ldapEnv.put(Context.SECURITY_PRINCIPAL, ldap_account);			// cn=admin,dc=placeoweb
        ldapEnv.put(Context.SECURITY_CREDENTIALS, ldap_password);		// motdepasse
        this.ldap_account = ldap_account;
    }

    public Hashtable<String, String> getLdapEnv() {
        return ldapEnv;
    }

    public Attributes getUserInfo(Hashtable ldapEnv, String login) {

        try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);
            System.out.println("LDAP : Bind Ok = ");
            // Recherche en profondeur
            // http://www.dil.univ-mrs.fr/~massat/ens/jee/ldap.html
            SearchControls controle = new SearchControls();
            controle.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //			 String critere = "(|(sn=premier)(sn=deux*))";
            //			 String critere = "(cn = *)";	// ne trouve rien
            String critere = "(cn=" + login + ")"; // ok					ldapsearch -x -h localhost -b 'dc=placeoweb' '(cn=*)'
            //			 DirContext ictx = new InitialDirContext(ldapEnv);
            //			 NamingEnumeration<SearchResult> e = ldapContext.search("ou=monorganisationunit,dc=placeoweb", critere, controle);
            NamingEnumeration<SearchResult> e = ldapContext.search(Play.configuration.getProperty("ldap.dn"), critere, controle);
            while (e.hasMore()) {
                SearchResult r = e.next();
                System.out.println("name: " + r.getName());
                System.out.println("object: " + r.getClassName());
                System.out.println("getAttributes: " + r.getAttributes());
                //System.out.println("bigsiri: " + r.getAttributes().get("userPassword").getID());
                //listerAttributs(r.getAttributes());

                return r.getAttributes();
            }

        } catch (AuthenticationException error) {
            return null;

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static HashMap<String, String> getConnectedUserInfos(String username){
    	HashMap<String, String> infos=new HashMap<String, String>();
    	if(username.equals("admin")){
        	return null;
        }
		Ldap adminConnection = new Ldap();
		adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		Attributes r=adminConnection.getUserInfo(adminConnection.getLdapEnv(),username);
    	try{
    		NamingEnumeration  e=r.getAll();
    		while(e.hasMore()){
    			javax.naming.directory.Attribute a=(javax.naming.directory.Attribute)e.next();
    			String attributeName=a.getID();
    			String attributeValue="";
    			Enumeration values = a.getAll();
    			while(values.hasMoreElements()){
    				attributeValue = values.nextElement().toString();
    			}
				if(attributeName.equals("mail"))
				{
					infos.put("mail", attributeValue);
				}
				else if(attributeName.equals("givenName"))
				{
					infos.put("firstName", attributeValue);
				}
				else if(attributeName.equals("sn"))
				{
					infos.put("lastName", attributeValue);
				}
    		}
    	}catch(javax.naming.NamingException e) {
    		System.out.println(e.getMessage());
    		return null;
    	}
    	return infos;
    }

    public void modifyPassword(Hashtable ldapEnv, String password) {
        try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);

            Attributes attributes = new BasicAttributes(true);
            Attribute attribut = new BasicAttribute("userPassword");
            attribut.add(password);
            attributes.put(attribut);

            ldapContext.modifyAttributes(this.ldap_account,
                    DirContext.REPLACE_ATTRIBUTE, attributes);
            ldapContext.close();

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void modifyAttribute(Hashtable ldapEnv, String login, String attributeName, String value) {
        try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);

            Attributes attributes = new BasicAttributes(true);
            Attribute attribut = new BasicAttribute(attributeName);
            attribut.add(value);
            attributes.put(attribut);

            ldapContext.modifyAttributes("cn=" + login + "," + Play.configuration.getProperty("ldap.dn"),
                    DirContext.REPLACE_ATTRIBUTE, attributes);
            ldapContext.close();

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Attributes getGroupInfo(Hashtable<String, String> ldapEnv, String groupName) {
        try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);
            System.out.println("LDAP : Bind Ok = ");
            SearchControls controle = new SearchControls();
            controle.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String critere = "(cn=" + groupName + ")";
            NamingEnumeration<SearchResult> e = ldapContext.search(Play.configuration.getProperty("ldap.dn"), critere, controle);
            while (e.hasMore()) {
                SearchResult r = e.next();
                System.out.println("name: " + r.getName());
                System.out.println("object: " + r.getClassName());
                System.out.println("getAttributes: " + r.getAttributes());
                return r.getAttributes();
            }

        } catch (AuthenticationException error) {
            return null;

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    void addGroup(Hashtable<String, String> ldapEnv, String cn, ArrayList members, String owner) {
        Attributes attributes = new BasicAttributes(true);
        attributes.put(new BasicAttribute("objectClass", "groupOfNames"));
        attributes.put(new BasicAttribute("cn", cn));
        attributes.put(new BasicAttribute("owner", "cn=" + owner + "," + Play.configuration.getProperty("ldap.dn")));
        Iterator memberIterator = members.iterator();
		
		BasicAttribute membersAttribute = new BasicAttribute("member", "cn="+owner+","+Play.configuration.getProperty("ldap.dn"));

        while(memberIterator.hasNext()){
            String specificMember = "cn="+memberIterator.next()+","+Play.configuration.getProperty("ldap.dn");
            if(!membersAttribute.contains(specificMember))
                membersAttribute.add(specificMember);
        }
		
        attributes.put(membersAttribute);
        DirContext ldapContext = null;
        try {
            ldapContext = new InitialDirContext(ldapEnv);
            ldapContext.bind("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"), null, attributes);
            ldapContext.close();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'acces au serveur LDAP " + e);
            e.printStackTrace();
        }
        System.out.println("fin des traitements");
    }
	
	void addSpecificMember(Hashtable ldapEnv, String cn, ArrayList members, String specificMemberLogin) {
	
		try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);

            Attributes attributes = new BasicAttributes(true);
			
			String specificMember = "cn="+specificMemberLogin+","+Play.configuration.getProperty("ldap.dn");
            
			//We check that the member hasn't already been added in the group
			if(!members.contains(specificMember))
				members.add(specificMember);
			else System.out.println("Error: "+ specificMemberLogin + " already exists in this group.");

			BasicAttribute membersAttribute = new BasicAttribute("member");
			
			for(int i = 0; i < members.size(); i++)
			{
				membersAttribute.add(members.get(i));
			} 
			
			attributes.put(membersAttribute);

            ldapContext.modifyAttributes("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"),
                    DirContext.REPLACE_ATTRIBUTE, attributes);
            ldapContext.close();

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		System.out.println("fin des traitements");	
	}
	
	void removeSpecificMember(Hashtable ldapEnv, String cn, ArrayList members, String specificMemberLogin) {
	
		try {
            DirContext ldapContext = null;
            ldapContext = new InitialDirContext(ldapEnv);

            Attributes attributes = new BasicAttributes(true);
			
			BasicAttribute membersAttribute = new BasicAttribute("member");
			
			String memberLoginToRemove = "cn="+specificMemberLogin+","+Play.configuration.getProperty("ldap.dn");
			
			//Looking for the member's name in the list
			for(int i = 0; i < members.size(); i++)
			{
				if(memberLoginToRemove.equals(members.get(i)))
				members.remove(i);
			}
			
			//We rebuild a member attribute with the remaining members after the delete
			for(int i = 0; i < members.size(); i++)
			{
				membersAttribute.add(members.get(i));
			} 
			
			attributes.put(membersAttribute);

            ldapContext.modifyAttributes("cn=" + cn + "," + Play.configuration.getProperty("ldap.dn"),
                    DirContext.REPLACE_ATTRIBUTE, attributes);
            ldapContext.close();

        } catch (NamingException ex) {
            Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		System.out.println("fin des traitements");	
	}

    public void deleteGroup(Hashtable ldapEnv, String cn) {
        DirContext ldapContext = null;
        try {
            ldapContext = new InitialDirContext(ldapEnv);
            ldapContext.unbind(cn);
            ldapContext.close();
        } catch (NamingException e) {
            System.err.println("Erreur lors de l'acces au serveur LDAP" + e);
            e.printStackTrace();
        }
        System.out.println("fin des traitements");
    }
}
