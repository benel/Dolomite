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
		this.dolomiteURL = "http://"+lowerCasePrefix+Play.configuration.getProperty("domain");
	}
}	

