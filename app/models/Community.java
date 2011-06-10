package models;
 
import java.util.*;
import java.lang.*;
import javax.persistence.*;
import play.*;
import play.db.jpa.*;
import controllers.*;

@Entity 
public class Community extends Model {
 
	public String name;
	public String communityPrefix;
	public String dolomiteURL;
	@Lob
	public String descriptionText;
	@Lob
	public String welcomingMessage;
	public String applicationURL;
	
	public Community(String name, String communityPrefix, String applicationURL, String descriptionText, String welcomingMessage) {
		this.name = name;
		this.communityPrefix = communityPrefix.toUpperCase();
		this.descriptionText = descriptionText;
		this.welcomingMessage = welcomingMessage;
		this.applicationURL = applicationURL;
		String lowerCasePrefix = communityPrefix.toLowerCase();
		this.dolomiteURL = "http://" + lowerCasePrefix + ".community.hypertopic.org";
	}
	
	public String getName(){return name;}
	
	public void setName(String newName){
		this.name = newName;
	}

	public String getCommunityPrefix(){return communityPrefix;}
	
	public void setCommunityPrefix(String newCommunityPrefix){
		this.communityPrefix = newCommunityPrefix;
	}
	
	public String getDescriptionText(){return descriptionText;}
	
	public void setDescriptionText(String newDescriptionText) {
		this.descriptionText = newDescriptionText;
	}
	
	public String getWelcomingMessage(){return welcomingMessage;}
	
	public void setWelcomingMessage(String newWelcomingMessage) {
		this.welcomingMessage = newWelcomingMessage;
	}
	
	public String getApplicationURL(){return applicationURL;}
	
	public void setApplicationURL(String newApplicationURL) {
		this.applicationURL = newApplicationURL;
	}
	
	public String getDolomiteURL(){return dolomiteURL;}
	
	public void setDolomiteURL(String communityPrefix) {
		String lowerCasePrefix = communityPrefix.toLowerCase();
		this.dolomiteURL = "http://" + lowerCasePrefix + ".community.hypertopic.org";		
	}
	

}	

