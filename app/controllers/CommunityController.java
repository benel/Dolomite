package controllers;

import java.net.URLEncoder;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.*;
 
import play.*;
import play.mvc.*;
import play.libs.Crypto;
import play.libs.Mail;
import play.data.validation.*;
import java.io.UnsupportedEncodingException;

import notifiers.*;
import models.*;
import play.*;
import play.i18n.Messages;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;

@With(Secure.class)
public class CommunityController extends Controller {

	public static void index() {
	
		List<Community> communities_list = Community.findAll();
		
		render("Community/index.html", communities_list);
	}
	
	public static void createCommunityIndex() {
		render("Community/createCommunityIndex.html");
	}
	
	public static void createNewCommunity(
	  @Required String nom_communaute,
	  @Required String prefixe_communaute,
	  @Required String application_link,
	  @Required String message_description,
	  @Required String message_bienvenue
	){
	 
		try {
		
			if (validation.hasErrors()){
                render("Community/createCommunityIndex.html");
            } 
			else {
				
				Community communaute = new Community (
				nom_communaute,
				prefixe_communaute,
				application_link,
				message_description,
				message_bienvenue).save();				
				
				flash.success(Messages.get("create_community_success"));
				
				render("Community/createCommunityIndex.html");
				//index();
			}
	   
        } catch (Exception e) {
		System.out.println("An exception occurred in CommunityController.createNewCommunity");
		e.printStackTrace();
		render("Community/createCommunityIndex.html"); }
		
	}
	
	public static void displayCommunityUpdate(Long id) {
	
		Community community_retrieved = Community.findById(id);
		
		render("Community/updateCommunityIndex.html", community_retrieved);
	
	}
	
	public static void updateCommunity(
	  Long id,
	  String nom_communaute,
	  String prefixe_communaute,
	  String application_link,
	  String message_description,
	  String message_bienvenue){
	
		Community community_updated = Community.findById(id);
		
		// Updating the community attributes with the new ones
		if (nom_communaute != null) {
			community_updated.name = nom_communaute;
		}
		
		if (prefixe_communaute != null) {
			community_updated.communityPrefix = prefixe_communaute.toUpperCase();
			
			// Updating the dolomiteURL of the community
			community_updated.setDolomiteURL(prefixe_communaute);
			
		}
		
		if (message_description != null) {
			community_updated.descriptionText = message_description;
		}
		
		if (message_bienvenue != null) {
			community_updated.welcomingMessage = message_bienvenue;
		}
		
		if (application_link != null) {
			community_updated.applicationURL = application_link;
		}
		
		community_updated.save();
		
		index();		
	}
	
	public static void deleteCommunity(Long id) {
	
		Community community_deleted = Community.findById(id);		
		community_deleted.delete();
		
		index();
	}
	
	public static void searchCommunity( @Required String community_name_search) {
	
		String community_prefix_upper_case = community_name_search.toUpperCase();
		
		Community community_searched = Community.find("byCommunityPrefix", community_prefix_upper_case).first();		
			
		render("Community/resultsSearch.html", community_searched);		

	}

	
}
