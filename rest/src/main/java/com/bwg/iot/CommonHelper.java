package com.bwg.iot;

import com.bwg.iot.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gluu.scim.client.ScimClient;
import gluu.scim.client.ScimResponse;
import gluu.scim.client.model.ScimCustomAttributes;
import gluu.scim.client.model.ScimName;
import gluu.scim.client.model.ScimPerson;
import gluu.scim.client.model.ScimPersonEmails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
public class CommonHelper {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(CommonHelper.class);
    @Autowired
    DealerRepository dealerRepository;

    @Autowired
    OemRepository oemRepository;

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    EntityLinks entityLinks;

    /**
     * generates Link to logo for a given user
     *
     * First checks for a dealer logo, then will fall back to oem logo
     * @param user
     * @return
     */
    public Link getMyLogoLink(User user) {
        Link logoLink = null;
        if (user.getDealerId() != null) {
            Dealer dealer = dealerRepository.findOne(user.getDealerId());
            if (dealer != null && dealer.getLogo() != null) {
                logoLink = entityLinks.linkToSingleResource(Attachment.class, dealer.getLogo().get_id()).withRel("logo");
            }
        }
        if (logoLink == null && user.getOemId() != null) {
            Oem oem = oemRepository.findOne(user.getOemId());
            if (oem != null && oem.getLogo() != null) {
                logoLink = entityLinks.linkToSingleResource(Attachment.class, oem.getLogo().get_id()).withRel("logo");
            }
        }
        return logoLink;
    }

    /**
     * generate link to user's spa
     * @param user
     * @return
     */
    public Link getMySpaLink(User user) {
        if (user.hasRole(User.Role.OWNER.toString())) {
            Spa spa = spaRepository.findByUsername(user.getUsername());
            if (spa != null) {
                return entityLinks.linkToSingleResource(Spa.class, spa.get_id()).withRel("spa");
            }
        }
        return null;
    }
}
