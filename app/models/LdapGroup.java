package models;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import play.Play;

public class LdapGroup {

    private ArrayList<String> members;
    private String groupName;
    private String owner;
    
    public LdapGroup(String groupName,ArrayList<String> members,String owner){
        this.groupName=groupName;
        this.members=members;
        this.owner=owner;
    }

    public static LdapGroup retrieve(String groupName){
        Ldap adminConnection = new Ldap();
        adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));

        LdapGroup newGroup;
        ArrayList<String> members= new ArrayList<String>();
        String owner="";

        Attributes atts=adminConnection.getGroupInfo(adminConnection.getLdapEnv(), groupName);
        if(atts==null){
            return null;
        }

        try {
            for (NamingEnumeration e = atts.getAll(); e.hasMore();){
                Attribute a = (Attribute) e.next();
                String attributeName = a.getID();
                String attributeValue = "";
                Enumeration values = a.getAll();
                while (values.hasMoreElements()) {
                    attributeValue = values.nextElement().toString();
                    System.out.println(attributeName+" value --> " + attributeValue.toString());
                }

                if(attributeName.equals("owner")){
                    owner = attributeValue;
                }
                else if(attributeName.equals("member")){
                    members.add(attributeValue);
                }
            }
            newGroup = new LdapGroup(groupName,members,owner);
            return newGroup;
        }catch (javax.naming.NamingException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static int createGroup(String groupName, ArrayList members, LdapUser owner) {
		Ldap adminConnection = new Ldap();
		
		adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		if(adminConnection.getGroupInfo(adminConnection.getLdapEnv(),groupName)!=null){
			return 1 ;
		}
		else{
			adminConnection.addGroup(adminConnection.getLdapEnv(), groupName, members, owner.getLogin());
			return 0;
		}
    }
	
	public void addSpecificMember(String newMemberLogin) {
	
		Ldap adminConnection = new Ldap();
		
		adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		adminConnection.addSpecificMember(adminConnection.getLdapEnv(), this.groupName, this.members, newMemberLogin);		
		
	}
	
	public void removeSpecificMember(String memberLogin) {
	
		Ldap adminConnection = new Ldap();
		
		adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		adminConnection.removeSpecificMember(adminConnection.getLdapEnv(), this.groupName, this.members, memberLogin);		
		
	}
	
	public void displayGroupMembers() {
	
		System.out.println();
		System.out.println("----- Members of the group "+ this.groupName +" -----");
		System.out.println("------------------------------------------------------------------ ");
		
		for(int i = 0; i < this.members.size(); i++)
		{
			System.out.println("Member number " + (i+1) + " : " + this.members.get(i));
			System.out.println("------------------------------------------------------------------ ");
		}
	}


    public void deleteGroup(){
		Ldap adminConnection = new Ldap();
		adminConnection.SetEnv(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		adminConnection.deleteGroup(adminConnection.getLdapEnv(), "cn="+groupName+","+Play.configuration.getProperty("ldap.dn"));
	}
}
