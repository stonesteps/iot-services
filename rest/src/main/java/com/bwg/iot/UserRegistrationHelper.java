/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.bwg.iot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gluu.scim.client.ScimResponse;
import gluu.scim.client.model.ScimName;
import gluu.scim.client.model.ScimPerson;
import gluu.scim.client.model.ScimPersonEmails;
import gluu.scim2.client.Scim2Client;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.gluu.oxtrust.model.scim2.Email;
import org.gluu.oxtrust.model.scim2.Group;
import org.gluu.oxtrust.model.scim2.GroupRef;
import org.gluu.oxtrust.model.scim2.Name;
import org.gluu.oxtrust.model.scim2.PhoneNumber;
import org.gluu.oxtrust.model.scim2.Role;
import org.gluu.oxtrust.model.scim2.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationHelper {
  
  private final static org.slf4j.Logger log = LoggerFactory.getLogger(UserRegistrationHelper.class);
  
  @Autowired
          Environment environment;
  
  private Scim2Client scimClient;
  private String domain;
  private String umaMetaDataUrl;
  private String umaAatClientId;
  private String umaAatClientKeyId;
  private String umaAatClientJwks;
  private String openidKeysFilename;
  
  @PostConstruct
  public void initialize() {
    try {
      createScimClient();
    } catch (Throwable t) {
      log.error("error in service initialization");
    }
  }
  
  /**
   * Create the Gluu client using configuration parameters. The client is defined
   * once at boot time.
   * @throws Throwable
   */
  private void createScimClient() throws Throwable {
    domain = environment.getProperty(PropertyNames.IDM_DOMAIN);
    umaMetaDataUrl = environment.getProperty(PropertyNames.UMA_METADATA_URL);
    umaAatClientId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_ID);
    umaAatClientKeyId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_KEY_ID);
    openidKeysFilename = environment.getProperty(PropertyNames.UMA_OPENID_KEYS_FILENAME);
    // process keys: the file is in the resources folder FIXME: make the filename dynamic?
    InputStream is = getClass().getResourceAsStream(openidKeysFilename);
    StringWriter writer = new StringWriter();
    IOUtils.copy(is, writer, "UTF8");
    umaAatClientJwks = writer.toString();
    // create client
    scimClient = Scim2Client.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);
    log.info("SCIM Client created: using key file " + openidKeysFilename);
  }
//  
//  private ScimPerson createPerson(com.bwg.iot.model.User user) {
//    ScimPerson person = new ScimPerson();
//    
//    if (null != user) {
////      person.setActive("true");
//      person.setUserName(user.getUsername());
//      person.setPassword(user.getUsername());
//      
//      ScimName name = new ScimName();
//      name.setFamilyName(user.getLastName());
//      name.setGivenName(user.getFirstName());
//      person.setName(name);
//      
//      ScimPersonEmails email = new ScimPersonEmails();
//      
////      email.setValue(user.getEmail());
//      email.setPrimary(user.getEmail());
//      person.getEmails().add(email);
//      
//    }
//    return person;
//  }
  
  /**
   * Convert a bwg user into a gluu one
   * @param user
   * @return user
   */
  private User convertUser(com.bwg.iot.model.User user) {
    User gluuUser = null;
    
    
    if (null != user) {
      gluuUser = new User();
   
      // user must be active
      gluuUser.setActive(Boolean.TRUE);
      // username
      gluuUser.setUserName(user.getUsername());
      // name
      Name name = new Name();
      name.setFamilyName(user.getLastName());
      name.setGivenName(user.getFirstName());
      gluuUser.setName(name);
      // password
      gluuUser.setPassword("adminadmin");
      // display name
      gluuUser.setDisplayName(user.getFullName());
      // email
      Email email = new Email();
      email.setValue(user.getEmail());
      email.setType(org.gluu.oxtrust.model.scim2.Email.Type.WORK);
      email.setPrimary(true);
      gluuUser.getEmails().add(email);
      // phone
      PhoneNumber phone = new PhoneNumber();
      phone.setType(org.gluu.oxtrust.model.scim2.PhoneNumber.Type.WORK);
      phone.setValue(user.getPhone());
      gluuUser.getPhoneNumbers().add(phone);
      // address
      org.gluu.oxtrust.model.scim2.Address address = new org.gluu.oxtrust.model.scim2.Address();
      address.setCountry(user.getAddress().getCountry());
      address.setStreetAddress(user.getAddress().getAddress1());
      address.setLocality(user.getAddress().getCity());
      address.setPostalCode(user.getAddress().getZip());
      address.setRegion(user.getAddress().getState());
      address.setPrimary(true);
      address.setType(org.gluu.oxtrust.model.scim2.Address.Type.WORK);
      address.setFormatted(address.getStreetAddress() + " " + address.getLocality() + " " + address.getPostalCode() + " " + address.getRegion() + " "
              + address.getCountry());
      gluuUser.getAddresses().add(address);
      // Group
      /*
      GroupRef group = new GroupRef();
//      Group group = new Role();
      group.setValue("@!AE19.F0AF.AA52.0416!0001!0EF9.F0BD!0003!60B7");
      group.setDisplay("Gluu Manager Group");
      gluuUser.getGroups().add(group);
      */
      gluuUser.setPreferredLanguage("US_us");
    }
    return gluuUser;
  }
  
  /**
   * Create a user in the Gluu server using SCIM-Client. This method uses the
   * the org.gluu.oxtrust.model.scim2.User object to pass data to the server.
   * @param user
   * @return the jsonNode containing the response recived
   * @throws Throwable
   */
  public JsonNode createUser(com.bwg.iot.model.User user) throws Throwable {
    ObjectMapper mapper = new ObjectMapper();
    log.info("Inside GluuHelper.createUser");
    User gluuUser = convertUser(user);
//    ScimPerson gluuperson = createPerson(user);
    
    JsonNode jsonNode = null;
    log.info("Calling SCIM createPerson");
    ScimResponse response = scimClient.createPerson(gluuUser, MediaType.APPLICATION_JSON);
    log.info("Back from SCIM createPerson");

    // throw exception if the code is not 2xx
    if (response.getStatusCode() < 200
            || response.getStatusCode() > 299) {
      log.error("SCIM-Client reported status " + response.getStatusCode());
      throw new RuntimeException("error condition returned by the SCIM-client");
    } else {
      log.info("SCIM-Client returned status " + response.getStatusCode());
    }
    // return a JsonNode object
    String body = response.getResponseBodyString();
    jsonNode = mapper.readTree(body);
    log.info("SCIM-Client returned the following payload " + body);
    return jsonNode;
  }
  
  public Scim2Client getScimClient() {
    return scimClient;
  }
  
  public String getDomain() {
    return domain;
  }
  
  public String getUmaMetaDataUrl() {
    return umaMetaDataUrl;
  }
  
  public String getUmaAatClientId() {
    return umaAatClientId;
  }
  
  public String getUmaAatClientKeyId() {
    return umaAatClientKeyId;
  }
  
  public String getUmaAatClientJwks() {
    return umaAatClientJwks;
  }
  
}
