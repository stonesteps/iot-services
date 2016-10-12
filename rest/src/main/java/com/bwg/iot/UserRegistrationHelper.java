package com.bwg.iot;

import com.bwg.iot.model.User;
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
import javax.ws.rs.core.MediaType;

import gluu.scim.client.util.Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class UserRegistrationHelper {

    public final static String PWD_CHANGE = "PWD_CHANGE";
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(UserRegistrationHelper.class);

    @Autowired
    Environment environment;

    @Autowired
    CommonHelper commonHelper;

    private String domain;
    private String umaMetaDataUrl;
    private String umaAatClientId;
    private String umaAatClientKeyId;
    private String umaAatClientJwks;
    private String openidKeysFilename;

    private Properties mailServerProperties;
    private Session getMailSession;
    private SecureRandom random;

    private String mailServer;
    private String mailerUsername;
    private String mailerPassword;
    private String spaPortalEndpoint;
    private String idmPortalEndpoint;
    private String activeProfile;

    @PostConstruct
    public void initialize() throws Exception {
        domain = environment.getProperty(PropertyNames.IDM_DOMAIN);
        umaMetaDataUrl = environment.getProperty(PropertyNames.UMA_METADATA_URL);
        umaAatClientId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_ID);
        umaAatClientKeyId = environment.getProperty(PropertyNames.UMA_AAT_CLIENT_KEY_ID);
        openidKeysFilename = environment.getProperty(PropertyNames.UMA_OPENID_KEYS_FILENAME);
        activeProfile = environment.getProperty(PropertyNames.ACTIVE_PROFILES);

        log.info("SCIM Client using key file " + openidKeysFilename);

        InputStream is = getClass().getResourceAsStream(openidKeysFilename);
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF8");
        umaAatClientJwks = writer.toString();

        // email
        mailServer = environment.getProperty(PropertyNames.SMTP_SERVER);
        mailServerProperties = System.getProperties();
        mailServerProperties.put(PropertyNames.SMTP_PORT, environment.getProperty(PropertyNames.SMTP_PORT));
        mailServerProperties.put(PropertyNames.SMTP_AUTH, environment.getProperty(PropertyNames.SMTP_AUTH));
        mailServerProperties.put(PropertyNames.SMTP_TLS, environment.getProperty(PropertyNames.SMTP_TLS));
        mailerUsername = environment.getProperty(PropertyNames.MAIL_USERNAME);
        mailerPassword = environment.getProperty(PropertyNames.MAIL_PASSWORD);
        spaPortalEndpoint = environment.getProperty(PropertyNames.SPA_PORTAL_ENDPOINT);
        idmPortalEndpoint = environment.getProperty(PropertyNames.IDM_PORTAL_ENDPOINT);
        log.info("email parameters loaded");

        // other expensive setup
        random = new SecureRandom();
    }

    /**
     * Generate a random password
     *
     * @return
     */
    private String generateRandomPassword() {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

        StringBuffer pw = new StringBuffer("");
        for (int i=0; i<8; i++)
        {
            int index = (int)(random.nextDouble()*letters.length());
            pw.append(letters.charAt(index));
        }
        return pw.toString();
    }

    /**
     * Process mail tempate
     *
     * @param person
     * @param role
     * @return
     */
    private String getMailTemplate(ScimPerson person, String role) {
        String template = "";

        if (User.Role.OWNER.toString().equalsIgnoreCase(role)){
            template = environment.getProperty(PropertyNames.MAIL_TEMPLATE_OWNER);
        } else if (PWD_CHANGE.equalsIgnoreCase(role)) {
            template = environment.getProperty(PropertyNames.MAIL_TEMPLATE_PWD);
        } else {
            template = environment.getProperty(PropertyNames.MAIL_TEMPLATE_EMPLOYEE);
        }

        // perform substitution
        if (StringUtils.isNotBlank(template)) {
            template = String.format(template, person.getDisplayName(), spaPortalEndpoint, person.getUserName(), person.getPassword());
        }
        return template;
    }

    public void sendGmailRegistrationMail(ScimPerson person, String role) throws AddressException, MessagingException {
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(person.getEmails().get(0).getValue()));
        generateMailMessage.setSubject(environment.getProperty(PropertyNames.MAIL_TEMPLATE_SUBJECT));
        String emailBody = getMailTemplate(person, role);
        generateMailMessage.setContent(emailBody, "text/html");

        Transport transport = getMailSession.getTransport("smtp");

        // if you have 2FA enabled then provide App Specific Password
        //		transport.connect("smtp.gmail.com", "controlmyspa@gmail.com", "4thoseabout2rock");
        transport.connect(mailServer, mailerUsername, mailerPassword);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    public void sendUserDeletedMail(ScimPerson person, String by) throws AddressException, MessagingException {
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(person.getEmails().get(0).getValue()));
        generateMailMessage.setSubject(environment.getProperty(PropertyNames.MAIL_TEMPLATE_SUBJECT_DELETED));
        String template = environment.getProperty(PropertyNames.MAIL_TEMPLATE_DELETED);
        if (StringUtils.isNotBlank(template)) {
            template = String.format(template, person.getDisplayName(), by);
        }
        generateMailMessage.setContent(template, "text/html");

        Transport transport = getMailSession.getTransport("smtp");

        // if you have 2FA enabled then provide App Specific Password
        //		transport.connect("smtp.gmail.com", "controlmyspa@gmail.com", "4thoseabout2rock");
        transport.connect(mailServer, mailerUsername, mailerPassword);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }


    private ScimPerson createPerson(com.bwg.iot.model.User user) throws Throwable {
        ScimPerson person = new ScimPerson();
        String password = ("dev".equalsIgnoreCase(activeProfile)) ? user.getUsername() : generateRandomPassword();

        if (null != user) {
            List<String> schema = new ArrayList<String>();
            schema.add("urn:scim:schemas:core:1.0");
            person.setSchemas(schema);

            person.setUserName(user.getUsername());
            person.setPassword(password);
            person.setDisplayName(user.getFullName());

            ScimName name = new ScimName();
            name.setFamilyName(user.getLastName());
            name.setGivenName(user.getFirstName());
            person.setName(name);

            setPersonEmail(person, user);

            ScimCustomAttributes scimCustomAttributes = new ScimCustomAttributes();
            scimCustomAttributes.setName("gluuStatus");
            scimCustomAttributes.setValues(Arrays.asList(new String[]{"active"}));
            person.setCustomAttributes(Arrays.asList(scimCustomAttributes));
        }
        return person;
    }

    public void setPersonEmail(ScimPerson person, User user){
        ScimPersonEmails email = new ScimPersonEmails();

        log.info("Adding gluu person email: " + user.getEmail());
        email.setValue(user.getEmail());
        email.setType("Work");
        email.setPrimary("true");
        person.getEmails().add(email);
    }

    /**
     * Create a user in the Gluu server using SCIM-Client. This method uses the
     * the org.gluu.oxtrust.model.scim2.User object to pass data to the server.
     *
     * @param user
     * @return the jsonNode containing the response recived
     * @throws Throwable
     */
    public ScimPerson createUser(com.bwg.iot.model.User user) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        log.info("Inside GluuHelper.createUser");
        ScimPerson gluuperson = createPerson(user);

        // create client
        ScimClient scimClient = ScimClient.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);

        JsonNode jsonNode = null;
        log.info("Calling SCIM createPerson");
        ScimResponse response = scimClient.createPerson(gluuperson, MediaType.APPLICATION_JSON);
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
        return gluuperson;
    }

    public ScimPerson findPerson(com.bwg.iot.model.User user) throws Throwable {
        ScimClient scimClient = ScimClient.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);
        ScimResponse response = scimClient.personSearch("userid", user.getUsername(), MediaType.APPLICATION_JSON);
//        ScimResponse response = scimClient.retrieveAllPersons(MediaType.APPLICATION_JSON);

        // throw exception if the code is not 2xx
        if (response.getStatusCode() < 200 || response.getStatusCode() > 299) {
            log.error("SCIM-Client reported status " + response.getStatusCode());
            throw new RuntimeException("SCIM-client returned error: " + response.getStatusCode());
        } else {
            log.info("SCIM-Client returned status " + response.getStatusCode());
        }
        ScimPerson personRetrieved = (ScimPerson) commonHelper.jsonToObject(response, ScimPerson.class);
        return personRetrieved;
    }

    public ScimPerson updatePerson(ScimPerson person) throws Throwable {
        ScimClient scimClient = ScimClient.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);
        ScimResponse response = scimClient.updatePerson(person, person.getId(), MediaType.APPLICATION_JSON);

        // throw exception if the code is not 2xx
        if (response.getStatusCode() < 200
                || response.getStatusCode() > 299) {
            log.error("SCIM-Client reported status " + response.getStatusCode());
            throw new RuntimeException("SCIM-client returned error: " + response.getStatusCode());
        } else {
            log.info("SCIM-Client returned status " + response.getStatusCode());
        }
        ScimPerson personRetrieved = (ScimPerson) commonHelper.jsonToObject(response, ScimPerson.class);
        return personRetrieved;
    }

    public ScimPerson deletePerson(ScimPerson person) throws Throwable {
        ScimClient scimClient = ScimClient.umaInstance(domain, umaMetaDataUrl, umaAatClientId, umaAatClientJwks, umaAatClientKeyId);
        ScimResponse response = scimClient.deletePerson(person.getId());

        // throw exception if the code is not 2xx
        if (response.getStatusCode() < 200
                || response.getStatusCode() > 299) {
            log.error("SCIM-Client attempt to delete person reported status " + response.getStatusCode());
            throw new RuntimeException("SCIM-client returned error: " + response.getStatusCode());
        } else {
            log.info("SCIM-Client deletePerson " + person.getId() +  ", returned status " + response.getStatusCode());
        }
        ScimPerson personReturned = (ScimPerson) commonHelper.jsonToObject(response, ScimPerson.class);
        return personReturned;
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
