package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import models.Sign;
import play.mvc.*;
import play.data.validation.Required;
import play.libs.Crypto;
import models.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import play.*;


public class BaseController extends Controller {
   
    @Before
    public static void getDomain(){
        String domain = request.domain;
        String domainName = Play.configuration.getProperty(domain + ".name");
        String domainHref = Play.configuration.getProperty(domain + ".href");
        System.out.println(domainName);
        System.out.println(domainHref);
        renderArgs.put("domainName", domainName);
        renderArgs.put("domainHref", domainHref);
    }
}
