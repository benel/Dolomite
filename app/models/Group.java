package models;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import play.Play;

import controllers.*;

public class Group extends Entry{

    private ArrayList<String> members;
    private String groupName;
    private String owner;

    public Group(String groupName,ArrayList<String> members,String owner){
        this.groupName=groupName;
        this.members=members;
        this.owner=owner;
    }

	public String getGroupName(){return groupName;}
	public String getOwner(){return owner;}
	public ArrayList<String> getMembers(){return members;}
	
	public void setGroupName(String groupName){this.groupName = groupName;}
	public void setOwner(String owner){this.owner = owner;}
	public void setMembers(ArrayList<String> members){this.members = members;}


	public void addMember(String memberLogin) {

		this.members.add(memberLogin);

	}

	public void removeMember(String memberLogin) {

		this.members.remove(memberLogin);

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

    @Override
    public BasicAttributes toLDAPAttributes() {
        Attributes attributes = new BasicAttributes(true);
        attributes.put(new BasicAttribute("objectClass", "groupOfNames"));
        attributes.put(new BasicAttribute("cn", this.groupName));
        attributes.put(new BasicAttribute("owner", "cn=" + owner + "," + Play.configuration.getProperty("ldap.dn")));
        Iterator memberIterator = members.iterator();
		BasicAttribute membersAttribute = new BasicAttribute("member", "cn="+owner+","+Play.configuration.getProperty("ldap.dn"));

        while(memberIterator.hasNext()){
            String specificMember = "cn="+memberIterator.next()+","+Play.configuration.getProperty("ldap.dn");
            if(!membersAttribute.contains(specificMember))
                membersAttribute.add(specificMember);
        }

        attributes.put(membersAttribute);
        return (BasicAttributes)attributes;
    }



}
