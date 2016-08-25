package com.bwg.iot;

import com.bwg.iot.model.*;
import gluu.scim.client.ScimResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;


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
    UserRepository userRepository;

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

    public static Object jsonToObject(ScimResponse response, Class<?> clazz) throws Exception {
        byte[] bytes = response.getResponseBody();
        String json = new String(bytes);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        Object clazzObject = mapper.readValue(json, clazz);

        return clazzObject;
    }

    public Pair<String, String> getUserQualifier(String remote_user) {
        User remoteUser = userRepository.findByUsername(remote_user);
        if (remoteUser == null) {
            throw new IllegalStateException("Remote User " + remote_user + " not found");
        }
        String key;
        String value;
        if (remoteUser.hasRole(User.Role.BWG.name())) {
            return null;
        } else if (remoteUser.hasRole(User.Role.OEM.name())) {
            key = "oemId";
            value = remoteUser.getOemId();
        } else if (remoteUser.hasRole(User.Role.DEALER.name())) {
            key = "dealerId";
            value = remoteUser.getDealerId();
        } else if (remoteUser.hasRole(User.Role.OWNER.name())) {
            key = "spaId";
            value = remoteUser.getSpaId();
        } else {
            throw new IllegalStateException("Remote User " + remote_user + " has invalid roles");
        }

        if (value == null){
            throw new IllegalStateException("Remote User " + remote_user + " not authorized");
        }

        return new ImmutablePair(key, value);
    }
}
