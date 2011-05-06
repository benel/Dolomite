import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;



public class LdapTest extends UnitTest {

	@Before
	//to do before EACH test
	public void setUp(){
		new LdapUser("flora.dupont@utt.fr", "test", "Flora", "Dupont", "flora.dupont").addUser();
                
                LdapUser flo = LdapUser.connect("flora.dupont", "test");
                ArrayList<String> myMembers = new ArrayList();
                myMembers.add(flo.getLogin());
                LdapGroup.createGroup("myFirstGroup", myMembers, flo);
	}
	
	@Test
    public void createUser() 
	{
		int result = new LdapUser("firstname.lastname@utt.fr", "password", "Firstname", "Lastname", "firstname.lastname").addUser();
		assertEquals(0, result);	    
    }
	
	@Test
	public void connect_OK() {
		LdapUser flo = LdapUser.connect("flora.dupont", "test");
		assertNotNull(flo);
	}
	
	@Test
	public void connect_KO_no_user() {
		LdapUser wrong_user = LdapUser.connect("wrong.user", "wrong_password");
		assertNull(wrong_user);	
	}
	
	@Test
	public void connect_KO_wrong_password() {
		LdapUser flo = LdapUser.connect("flora.dupont", "wrong_password");
		assertNull(flo);	
	}
	
	@Test
    public void retrieveUserInfo() 
	{
		LdapUser flo = LdapUser.connect("flora.dupont", "test");	
		assertEquals("Flora", flo.getFirstname());
		assertEquals("Dupont", flo.getLastname());
		assertEquals("flora.dupont@utt.fr", flo.getEmail()); 
    }
	
	@Test 
	public void updateUserInfo(){
		LdapUser flo = LdapUser.connect("flora.dupont", "test");
		
		//update information
		flo.updateUser("flora.dupont@utt.fr", "new_password", "Arolf", "Tnopud");

		//try to connect using old and new password
		LdapUser floModified = LdapUser.connect("flora.dupont", "new_password");
		LdapUser floWithOldPwd = LdapUser.connect("flora.dupont", "test");
		
		assertNull(floWithOldPwd);
		assertNotNull(floModified);
		assertEquals("flora.dupont@utt.fr", floModified.getEmail());
		assertEquals("Arolf", floModified.getFirstname());
		assertEquals("Tnopud", floModified.getLastname());
	}
	
	@Test
    public void deleteUser() 
	{
		new LdapUser("firstname.lastname@utt.fr", "password", "Firstname", "Lastname", "firstname.lastname").addUser();
		
		LdapUser user = LdapUser.connect("firstname.lastname", "password");
		user.deleteUser();
		LdapUser userDeleted = LdapUser.connect("firstname.lastname", "password");
		
		assertNotNull(user); 
		assertNull(userDeleted); 
    }
	
	@Test
    public void createGroup(){
        LdapUser flo = LdapUser.connect("flora.dupont", "test");
        ArrayList<String> myMembers = new ArrayList();
        myMembers.add(flo.getLogin());

        int aGroup = LdapGroup.createGroup("MySecondGroup", myMembers, flo);
        assertEquals(0, aGroup);
    }
	
	@Test
    public void addSpecificMemberInGroup(){
        
		System.out.println("------------ Unit Test < Add a New Member in a Group > ------------");
		
		LdapUser flo = LdapUser.connect("flora.dupont", "test");
        ArrayList<String> myMembers = new ArrayList();
		
        new LdapUser("steve.bobbs@utt.fr", "password", "Steve", "Bobbs", "steve.bobbs").addUser();
		LdapUser steve = LdapUser.connect("steve.bobbs", "password");
		
		new LdapUser("michel.vaillant@utt.fr", "password", "Michel", "Vaillant", "michel.vaillant").addUser();
		LdapUser michel = LdapUser.connect("michel.vaillant", "password");
		
		myMembers.add(flo.getLogin());
		
		LdapGroup.createGroup("MySecondGroup", myMembers, flo);

		LdapGroup myGroup = LdapGroup.retrieve("mySecondGroup");
		myGroup.displayGroupMembers();

		myGroup.addSpecificMember(steve.getLogin());
		myGroup.displayGroupMembers();
		
		myGroup.addSpecificMember(michel.getLogin());
		myGroup.displayGroupMembers();

		
    }
	
	@Test
    public void deleteGroup(){
        LdapGroup myGroup = LdapGroup.retrieve("myFirstGroup");
        myGroup.deleteGroup();
        LdapGroup myGroup2 = LdapGroup.retrieve("myFirstGroup");
        assertNull(myGroup2);
    }
	
	@After
	//to do after EACH test
	public void setDown(){
		//delete Flora from Ldap
		LdapUser flo = LdapUser.connect("flora.dupont", "test");
		if(flo==null)
			flo = LdapUser.connect("flora.dupont", "new_password");	
		flo.deleteUser();
		
		//delete the user created during the test if any
		LdapUser user = LdapUser.connect("firstname.lastname", "password");
		if(user!=null)
			user.deleteUser();
		
                LdapGroup myGroup = LdapGroup.retrieve("myFirstGroup");
                if(myGroup!=null)
                    myGroup.deleteGroup();

                LdapGroup myGroup2 = LdapGroup.retrieve("mySecondGroup");
                if(myGroup2!=null)
                    myGroup2.deleteGroup();
	}

}
