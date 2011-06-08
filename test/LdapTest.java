
import javax.naming.NamingException;
import org.junit.*;
import java.util.*;
import javax.naming.AuthenticationException;
import play.test.*;
import play.*;
import models.*;



public class LdapTest extends UnitTest {

private LDAPDirectory adminConnection;

    @Before
    //to do before EACH test
    public void setUp() throws NamingException{
      adminConnection=new LDAPDirectory(Play.configuration.getProperty("ldap.host"),Play.configuration.getProperty("ldap.admin.dn"), Play.configuration.getProperty("ldap.admin.password"));
		 
		 User flora = new User("flore.dupont@utt.fr", "test", "Flora", "Dupont", "flore.tr");
		 Entry entryUser= (Entry)flora;
		 adminConnection.create(entryUser);

         ArrayList<String> myMembers = new ArrayList();
         myMembers.add(flora.getLogin());
         myMembers.add("steph");
         myMembers.add("toto");
		 Group floGroup=new Group("myFirstGroup", myMembers, flora.getLogin());
		 Entry entry=(Entry)floGroup;
         adminConnection.create(entry);
    }

     @Test
    public void createUser() throws NamingException {
        User usr = new User("firstname.userLastname@utt.fr", "password", "Firstname", "userLastname", "firstname.userLast");
        Entry entry=(Entry)usr;
        adminConnection.create(entry);
                
         LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"firstname.userLast"+","+Play.configuration.getProperty("ldap.dn"), "password");
        User flo2 = (User) userConnection.retrieve("firstname.userLast");
        
         assertNotNull(flo2);  
    }  

     
           @Test
    public void createGroup() throws NamingException {
        
        ArrayList<String> myMembers = new ArrayList();
         myMembers.add("flora.dupont");
        Group floGroup2=new Group("mySecondGroup", myMembers, "flora.tr");
         Entry entry=(Entry)floGroup2;
         adminConnection.create(entry);
        Group myGroup = (Group) adminConnection.retrieve("mySecondGroup");
        assertNotNull(myGroup); 
        
      }
     
      @Test
    public void connect_OK() throws NamingException  {

        LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"flore.tr"+","+Play.configuration.getProperty("ldap.dn"), "test");
		User flo = (User)userConnection.retrieve("flore.tr");
        assertNotNull(flo);
    }
      
    @Test
    public void connect_KO_no_user() throws NamingException {
        try{
        LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"wron_user"+","+Play.configuration.getProperty("ldap.dn"), "wrong_password");
		User flo = (User)userConnection.retrieve("wron_user");
        assertNull(flo);
        } catch (AuthenticationException e) {} 
    }

    @Test
    public void connect_KO_wrong_password() throws NamingException{
       try {
        LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"flore.tr"+","+Play.configuration.getProperty("ldap.dn"), "wrong_password");
       User flo = (User)userConnection.retrieve("flore.tr");
        assertNull(flo);
       } catch (AuthenticationException e) {}  
        
    }

 @Test
    public void retrieveUser() throws NamingException {
	       LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"flore.tr"+","+Play.configuration.getProperty("ldap.dn"), "test");
        User user= (User) userConnection.retrieve("flore.tr");
        assertEquals("Flora", user.getFirstname());
        assertEquals("Dupont", user.getLastname());
        assertEquals("flore.dupont@utt.fr", user.getEmail());
    } 
    
  @Test
 public void retrieveGroup() throws NamingException {
 	   Group group = (Group)adminConnection.retrieve("myFirstGroup");
		assertEquals("myFirstGroup", group.getGroupName());
		assertEquals("flore.tr", group.getOwner());
		assertEquals("flore.tr", group.getMembers().get(0));
                assertEquals("steph", group.getMembers().get(1));
                assertEquals("toto", group.getMembers().get(2));
;
 	  } 
 
 @Test
    public void updateUser() throws NamingException {
       LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"flore.tr"+","+Play.configuration.getProperty("ldap.dn"), "test");
       
		User user = (User) userConnection.retrieve("flore.tr");
        user.setEmail("kpstephie@yahoo.fr");
        user.setFirstname("stephanie");
        user.setLastname("kamwa");
        
        adminConnection.update((Entry)user);
        
        User newUser = (User) userConnection.retrieve("flore.tr");
        
        assertEquals("kpstephie@yahoo.fr", newUser.getEmail());
        assertEquals("stephanie", newUser.getFirstname());
        assertEquals("kamwa", newUser.getLastname());
        assertEquals("flore.tr", newUser.getLogin());
    }
    
    @Test
    public void updateGroup() throws NamingException {
       
		Group group = (Group) adminConnection.retrieve("myFirstGroup");
        group.setOwner("stephanie");
        group.getMembers().add("raf");
        
        adminConnection.update((Entry)group);
        
        Group newGroup = (Group) adminConnection.retrieve("myFirstGroup");
        
        assertEquals("stephanie", newGroup.getOwner());
        assertNotNull(newGroup.getMembers());
        assertEquals("raf", group.getMembers().get(3));
    }
 
    @Test
    public void deleteUser() throws NamingException  {
	LDAPDirectory userConnection =  new LDAPDirectory(Play.configuration.getProperty("ldap.host"),"cn="+"flore.tr"+","+Play.configuration.getProperty("ldap.dn"), "test");
        
        Entry entry = userConnection.retrieve("flore.tr");
        adminConnection.delete(entry);
        User userDeleted = (User)userConnection.retrieve("flore.tr");
		userConnection.closeConnection();
        assertNotNull(entry);
        assertNull(userDeleted);
    }
    
    @Test
    public void deleteGroup() throws NamingException {
		
        Group group = (Group)adminConnection.retrieve("myFirstGroup");
       Entry entry=(Entry)group;
        adminConnection.delete(entry);
        Group groupDeleted = (Group)adminConnection.retrieve("myFirstGroup");
        
        assertNotNull(group);
        assertNull(groupDeleted);
    }
    
    @After
    //to do after EACH test
    public void setDown() throws NamingException{
	
		//delete user create 

        
          User user2= (User) adminConnection.retrieve("flore.tr");
	    if (user2 != null) {
            adminConnection.delete(user2);
        }
             User user3 = (User) adminConnection.retrieve("userLast");
	    if (user3 != null) {
            adminConnection.delete(user3);
        }
             User user4 = (User) adminConnection.retrieve("firstname.userLast");
	    if (user4 != null) {
            adminConnection.delete(user4);
        }
            
        Group grp=(Group)adminConnection.retrieve("mySecondGroup");
	    if (grp != null) {
            adminConnection.delete(grp);
        }
        
        Group grp2=(Group)adminConnection.retrieve("myFirstGroup");
	    if (grp2 != null) {
            adminConnection.delete(grp2);
        }
        
        adminConnection.closeConnection();
 
        

    }



    
}
