/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwg.iot;

import gluu.scim.client.ScimResponse;
import gluu.scim2.client.Scim2Client;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gluu.oxtrust.model.scim2.Email;
import org.gluu.oxtrust.model.scim2.PhoneNumber;
import org.gluu.oxtrust.model.scim2.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author entando
 */
@Component
public class UserRegistrationHelper {
 
  @Autowired
  Environment environment;
  
  
  public void createUser() throws Throwable {
    try {
//      System.out.println(">>> " + environment.getProperty(PropertyNames.IDM_DOMAIN));
//      System.out.println(">>> " + environment.getProperty(PropertyNames.UMA_METADATA_URL));
//      System.out.println(">>> " + environment.getProperty(PropertyNames.UMA_AAT_CLIENT_ID));
//      System.out.println(">>> " + environment.getProperty(PropertyNames.UMA_AAT_CLIENT_KEY_ID));
      // ResourceUtils.getFile(res.getURL().getFile());
      
      String domain = environment.getProperty(PropertyNames.IDM_DOMAIN);
      String umaMetaDataUrl = environment.getProperty(PropertyNames.UMA_METADATA_URL);
      String umaAatClientId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_ID);
      String umaAatClientKeyId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_KEY_ID);
      
      // process keys
      InputStream is = getClass().getResourceAsStream("/openid-keys.json");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer);
      String umaAatClientJwks = writer.toString();
//      System.out.println(">>> " + umaAatClientJwks);
    
      // FIXME move into another method
      // FIXME pass User object
      final Scim2Client scim2Client = Scim2Client.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);

//    ScimResponse response = scim2Client.personSearch("uid", "admin", MediaType.APPLICATION_JSON);
//    System.out.println("SCIM2: " + response.getResponseBodyString());
      User personToAdd = new User();
      personToAdd.setUserName("emanuele77");
        personToAdd.setPassword("test");
        personToAdd.setDisplayName("Scim2DisplayName2");

        Email email = new Email();
        email.setValue("superma77@yahoo.it");
        email.setType(org.gluu.oxtrust.model.scim2.Email.Type.WORK);
        email.setPrimary(true);
        personToAdd.getEmails().add(email);

        PhoneNumber phone = new PhoneNumber();
        phone.setType(org.gluu.oxtrust.model.scim2.PhoneNumber.Type.WORK);
        phone.setValue("3286935623");
        personToAdd.getPhoneNumbers().add(phone);

        org.gluu.oxtrust.model.scim2.Address address = new org.gluu.oxtrust.model.scim2.Address();
        address.setCountry("IT");
        address.setStreetAddress("Via Ingurtosu 8A");
        address.setLocality("Cagliari");
        address.setPostalCode("09121");
        address.setRegion("CA");
        address.setPrimary(true);
        address.setType(org.gluu.oxtrust.model.scim2.Address.Type.WORK);
        address.setFormatted(address.getStreetAddress() + " " + address.getLocality() + " " + address.getPostalCode() + " " + address.getRegion() + " "
                + address.getCountry());
        personToAdd.getAddresses().add(address);

        personToAdd.setPreferredLanguage("IT_it");

        org.gluu.oxtrust.model.scim2.Name name = new  org.gluu.oxtrust.model.scim2.Name();
        name.setFamilyName("SCIM");
        name.setGivenName("SCIM");
        personToAdd.setName(name);
      
        ScimResponse response = scim2Client.createPerson(personToAdd, MediaType.APPLICATION_JSON);
     
       System.out.println("@@@ RESULT "+ response.getStatusCode());
       System.out.println("@@@ BODY " + response.getResponseBodyString());
      
      
      
    } catch (IOException ex) {
      ex.printStackTrace();
    }
//    try {
//      InputStream is = getClass().getResourceAsStream("/storedProcedures.sql");
//    } catch (Throwable t) {
//      
//    }
    
  }
  
}
