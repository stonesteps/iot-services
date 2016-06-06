package com.bwg.iot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gluu.scim.client.ScimClient;
import gluu.scim.client.ScimResponse;
import gluu.scim.client.model.ScimCustomAttributes;
import gluu.scim.client.model.ScimName;
import gluu.scim.client.model.ScimPerson;
import gluu.scim.client.model.ScimPersonEmails;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.gluu.oxtrust.model.scim2.Email;
import org.gluu.oxtrust.model.scim2.Name;
import org.gluu.oxtrust.model.scim2.PhoneNumber;
import org.gluu.oxtrust.model.scim2.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

@Component
public class UserRegistrationHelper {

    private final static org.slf4j.Logger log = LoggerFactory.getLogger(UserRegistrationHelper.class);

    @Autowired
    Environment environment;

    private String domain;
    private String umaMetaDataUrl;
    private String umaAatClientId;
    private String umaAatClientKeyId;
    private String umaAatClientJwks;
    private String openidKeysFilename;
    
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;
    private SecureRandom random;
    
    private String username;
    private String password;


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
     *
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
        
        
        log.info("SCIM Client using key file " + openidKeysFilename);
        
        // email 
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        username = environment.getProperty("mail.username");
        password = environment.getProperty("mail.password");
        log.info("email parameters loaded");
        
        // other expensive setup
        random = new SecureRandom();
    }
    
    /**
     * Generate a random password
     * @return 
     */
    private String generateRandomPassword() {
      return new BigInteger(130, random).toString(32).substring(0, 8);
    }
    
    /**
     * Process mail tempate
     * @param user
     * @return 
     */
    private String getMailTemplate(com.bwg.iot.model.User user) {
      String template = "";
      
      // FIXME get localized template
      if (user.getAddress().getCountry().equals("XYZ")) {
          
      } else {
        // fallback language is English
        template = environment.getProperty("mail.template.en");
      }
      // perform substitution
      if (StringUtils.isNotBlank(template)) {
        template = String.format(template, user.getFirstName(),
                user.getUsername(),
                user.getPassword());
      }
      return template;
    }
    
    public void sendGmailRegistrationMail(com.bwg.iot.model.User user) throws AddressException, MessagingException {
      // 
      getMailSession = Session.getDefaultInstance(mailServerProperties, null);
      generateMailMessage = new MimeMessage(getMailSession);
      generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
      generateMailMessage.setSubject("Greetings from Balboa Water Grups");
      String emailBody = getMailTemplate(user);
      generateMailMessage.setContent(emailBody, "text/html");
      
      Transport transport = getMailSession.getTransport("smtp");

      // arrange mail properties
      mailServerProperties = System.getProperties();
      mailServerProperties.put("mail.smtp.port", "587");
      mailServerProperties.put("mail.smtp.auth", "true");
      mailServerProperties.put("mail.smtp.starttls.enable", "true");

      // if you have 2FA enabled then provide App Specific Password
  //		transport.connect("smtp.gmail.com", "controlmyspa@gmail.com", "4thoseabout2rock");
      transport.connect("smtp.gmail.com", username, password);
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
      transport.close();
    }
    

    private ScimPerson createPerson(com.bwg.iot.model.User user)  throws Throwable {
        createScimClient();
        ScimPerson person = new ScimPerson();
        String password = user.getUsername(); // generateRandomPassword();
        
        if (null != user) {
            List<String> schema = new ArrayList<String>();
            schema.add("urn:scim:schemas:core:1.0");
            person.setSchemas(schema);

            person.setUserName(user.getUsername());
            person.setPassword(password);
            user.setPassword(password);
            person.setDisplayName(user.getFullName());

            ScimName name = new ScimName();
            name.setFamilyName(user.getLastName());
            name.setGivenName(user.getFirstName());
            person.setName(name);

            ScimPersonEmails email = new ScimPersonEmails();

            log.info("Adding gluu person email: " + user.getEmail());
            email.setValue(user.getEmail());
            email.setType("Work");
            email.setPrimary("true");
            person.getEmails().add(email);

            ScimCustomAttributes scimCustomAttributes = new ScimCustomAttributes();
            scimCustomAttributes.setName("gluuStatus");
            scimCustomAttributes.setValues(Arrays.asList(new String[]{"active"}));
            person.setCustomAttributes(Arrays.asList(scimCustomAttributes));
        }
        return person;
    }

    /**
     * Convert a bwg user into a gluu one
     *
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
            gluuUser.setPreferredLanguage("US_us");
        }
        return gluuUser;
    }

    /**
     * Create a user in the Gluu server using SCIM-Client. This method uses the
     * the org.gluu.oxtrust.model.scim2.User object to pass data to the server.
     *
     * @param user
     * @return the jsonNode containing the response recived
     * @throws Throwable
     */
    public JsonNode createUser(com.bwg.iot.model.User user) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        log.info("Inside GluuHelper.createUser");
//        User gluuUser = convertUser(user);
        ScimPerson gluuperson = createPerson(user);
        
      // create client
      ScimClient scim1Client = ScimClient.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);

        JsonNode jsonNode = null;
        log.info("Calling SCIM createPerson");
        ScimResponse response = scim1Client.createPerson(gluuperson, MediaType.APPLICATION_JSON);
        log.info("Back from SCIM createPerson");

        // throw exception if the code is not 2xx
        if (response.getStatusCode() < 200
                || response.getStatusCode() > 299) {
            log.error("SCIM-Client reported status " + response.getStatusCode());
            throw new RuntimeException("SCIM-client returned error: " + response.getStatusCode());
        } else {
            log.info("SCIM-Client returned status " + response.getStatusCode());
        }
        // return a JsonNode object
        String body = response.getResponseBodyString();
        jsonNode = mapper.readTree(body);
        log.info("SCIM-Client returned the following payload " + body);
        return jsonNode;
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
