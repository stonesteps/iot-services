package com.bwg.iot;

import com.bwg.iot.model.*;
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
