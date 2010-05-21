package com.example.tests;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class InvitationKO(Authentification) extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("http://dolomite.hypertopic.org/", "*chrome");
	}
	public void testInvitationKO(Authentification)() throws Exception {
		selenium.open("/");
		verifyTrue(selenium.isTextPresent("DOLOMITE - Directories led by members"));
		selenium.click("link=Log in");
		verifyTrue(selenium.isTextPresent("User"));
		verifyTrue(selenium.isTextPresent("Password"));
		selenium.type("User", "ismaila");
		selenium.type("Password", "welcome");
		selenium.click("link=OK");
		verifyTrue(selenium.isTextPresent("Votre login ou votre mot de passe ne sont pas correcte"));
	}
}
