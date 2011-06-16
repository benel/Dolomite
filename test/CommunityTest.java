import org.junit.*;
import play.test.*;
import models.*;
import java.util.*; 
 
public class CommunityTest extends UnitTest {
 
    @Before
    public void setup() {
        Fixtures.deleteAll();
    }
	
	@Test
	public void createAndRetrieveCommunity() {
    
		// Create a new community and save it
		Community communaute1 = new Community ("Communaute des Mangeurs De Gorilles", "CMDG",
		"Nous sommes la communaute des mangeurs de gorilles", 
		"Bienvenue à vous, mangeur de gorille", 
		"mangeurDeGorille.com").save();
		
		Community communaute2 = new Community ("Communaute des Mangeurs De Sauterelles", "CMDS",
		"Nous sommes la communaute des mangeurs de Sauterelles", 
		"Bienvenue à vous, mangeur de sauterelles", 
		"mangeurDeSauterelles.com").save();
		
		Community communaute3 = new Community ("Communaute des Mangeurs De Chenilles", "CMDC",
		"Nous sommes la communaute des mangeurs de Chenilles", 
		"Bienvenue à vous, mangeur de chenilles", 
		"mangeurDeChenilles.com").save();
		
		Community communaute4 = new Community ("Communaute des Mangeurs De Poules", "CMDP",
		"Nous sommes la communaute des mangeurs de poules", 
		"Bienvenue à vous, mangeur de poules", 
		"mangeurDePoules.com").save();
		
		List<Community> communities_list = Community.findAll();
		
		long communityCount = Community.count();	
		System.out.println("Number of communities created: "+ communityCount);
			
		System.out.println("----------------------------------------------------------------------");
		
		for(int i = 0; i < communities_list.size(); i++) {
			System.out.println("Community number " + (i+1) + " = " + communities_list.get(i).name);
			System.out.println("----------------------------------------------------------------------");
			System.out.println("Community dolomite URL = " + communities_list.get(i).dolomiteURL);
			System.out.println("----------------------------------------------------------------------");
			System.out.println(); System.out.println();
		}
	
	}
	
	@Test
	public void updateCommunity(){
		
		// Create a new community and save it
		Community communaute1 = new Community ("Communaute des Mangeurs De Gorilles", "CMDG",
		"Nous sommes la communaute des mangeurs de gorilles", 
		"Bienvenue à vous, mangeur de gorille", 
		"mangeurDeGorille.com").save();
		
		Community communaute2 = new Community ("Communaute des Mangeurs De Sauterelles", "CMDS",
		"Nous sommes la communaute des mangeurs de Sauterelles", 
		"Bienvenue à vous, mangeur de sauterelles", 
		"mangeurDeSauterelles.com").save();
		
		// Retrieve the community with the email adress "mangeurDeGorille.com"
		Community communaute_retrieved = Community.find("byCommunityPrefix", "CMDG").first();
		
		// Test 
		assertNotNull(communaute_retrieved);
		
		System.out.println();
		communaute_retrieved.communityPrefix="DBZ";
		System.out.println("valuer du prefixe de la communaute: "+ communaute_retrieved.communityPrefix);
		
		System.out.println();
		System.out.println("----------------------------------------------------------------------");

	}
	
	
	
 
}