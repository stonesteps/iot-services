package com.bwg.iot.listeners;

import com.bwg.iot.AddressRepository;
import com.bwg.iot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by triton on 4/8/16.
 */
@RepositoryEventHandler
public class UserEventHandler {
    private static final Logger log = LoggerFactory.getLogger(UserEventHandler.class);

    @Autowired
    AddressRepository addressRepository;

    @HandleBeforeCreate
    public void handleUserCreate(User user) {
        log.debug("Before Create User: " + user.getUsername());

        // persist Address in its own collection
        user.setAddress(persistAddress(user.getAddress()));

        // set createdDate
        user.setCreatedDate(new Date());
    }

    @HandleBeforeCreate
    public void handleSpaTemplateCreate(SpaTemplate spaTemplate) {
        log.info("Before Create SpaTemplate: " + spaTemplate.getModel());
        removeSpaTemplateLinks(spaTemplate);

        // set createdDate
        spaTemplate.setCreationDate(new Date());
    }

    @HandleBeforeCreate
    public void handleOemCreate(Oem oem) {
        log.info("Before Create Oem: " + oem.getName());

        oem.setAddress(persistAddress(oem.getAddress()));
        oem.setCreatedDate(new Date());
    }

    @HandleBeforeSave
    public void handleOemUpdate(Oem oem) {
        log.info("Before Save Edit Oem: " + oem.getName());

        oem.setAddress(persistAddress(oem.getAddress()));
        oem.setModifiedDate(new Date());
    }

    @HandleBeforeCreate
    public void handleDealerCreate(Dealer dealer) {
        log.info("Before Create Dealer: " + dealer.getName());

        dealer.setAddress(persistAddress(dealer.getAddress()));
        dealer.setCreatedDate(new Date());
    }

    @HandleBeforeSave
    public void handleDealerUpdate(Dealer dealer) {
        log.info("Before Save Edit Dealer: " + dealer.getName());

        dealer.setAddress(persistAddress(dealer.getAddress()));
        dealer.setModifiedDate(new Date());
    }

    @HandleBeforeSave
    public void handleSpaTemplateUpdate(SpaTemplate spaTemplate) {
        removeSpaTemplateLinks(spaTemplate);
    }

    private void removeSpaTemplateLinks(SpaTemplate spaTemplate) {
        // Strip out any HATEOAS links in incoming object
        spaTemplate.getMaterialList().forEach( material -> { material.removeLinks(); });
        if (spaTemplate.getAttachments() != null) {
            spaTemplate.getAttachments().forEach(attachment -> {
                attachment.removeLinks();
            });
        }
        spaTemplate.removeLinks();
    }

    private Address persistAddress(Address address) {
        if (address != null) {
            address = addressRepository.save(address);
        }
        return address;
    }
}
