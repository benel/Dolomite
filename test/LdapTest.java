import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;



public class LdapTest extends UnitTest {
	private LdapUser flo;
	
	@Before
	public void setUp()
	{
		// Create a new user and save it
		new LdapUser("flora.dupont@utt.fr", "test", "Flora", "Dupont", "flora.dupont").addUser();
		flo = LdapUser.connect("flora.dupont", "test");
	
	}
	
	@After
	public void delete()
	{
		
		flo.deleteUser();
	}


	@Test
    public void createAndRetrieveUser() 
	{
				
		assertNotNull(flo); 
		assertEquals("Flora", flo.getFirstname());
		assertEquals("Dupont", flo.getLastname());
		assertEquals("flora.dupont@utt.fr", flo.getEmail());
	    
    }
	
	@Test
	public void tryConnectAsUser() {
		LdapUser stef = LdapUser.connect("stephane.batteux", "pas_le_bon");
		LdapUser flo2 = LdapUser.connect("flora.dupont", "mauvais_mot_de_passe");
		
		assertNotNull(flo);
		assertNull(stef);
		assertNull(flo2);	
	}
	
	@Test 
	public void tryUpdateUser(){
		LdapUser admin = LdapUser.connect("admin", "if052010");
		
		//assertEquals("Flora", flo.getFirstname());
		
		flo.updateUser("flora.dupont@utt.fr", "hehehe", "arolf", "tnopud");


		
		LdapUser floModified = LdapUser.connect("flora.dupont", "hehehe");
		LdapUser floWithOldPwd = LdapUser.connect("flora.dupont", "test");
		
		floModified.deleteUser();
		
		assertNull(floWithOldPwd);
		
		assertNotNull(floModified);
		assertEquals("flora.dupont@utt.fr", floModified.getEmail());
		assertEquals("arolf", floModified.getFirstname());
		assertEquals("tnopud", floModified.getLastname());
		
	}
	
	@Test
	public void tryDeleteUser(){
	
		LdapUser flo = LdapUser.connect("flora.dupont", "test");
		assertNotNull(flo);
				flo.deleteUser();

		
		LdapUser flo2 = LdapUser.connect("flora.dupont", "test");
		
		assertNull(flo2);
	
	}
	

}
