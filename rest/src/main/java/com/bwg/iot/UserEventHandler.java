package com.bwg.iot;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.SpaTemplate;
import com.bwg.iot.model.User;
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
        Address address = user.getAddress();
        if (address != null && StringUtils.isEmpty(address.get_id())) {
            address = addressRepository.save(address);
            user.setAddress(address);
        }

        // set createdDate
        user.setCreatedDate(new Date());
    }

    @HandleBeforeCreate
    public void handleSpaTemplateCreate(SpaTemplate spaTemplate) {
        log.debug("Before Create SpaTemplate: " + spaTemplate.getModel());

        // Strip out any HATEOAS links in incoming object
        spaTemplate.getMaterialList().stream().forEach( material -> { material.removeLinks(); });
        spaTemplate.removeLinks();

        // set createdDate
        spaTemplate.setCreationDate(new Date());
    }

    @HandleBeforeSave
    public void handleSpaTemplateSave(SpaTemplate spaTemplate) {
        log.debug("Before Save SpaTemplate: " + spaTemplate.getModel());

        // Strip out any HATEOAS links in incoming object
        spaTemplate.getMaterialList().stream().forEach( material -> { material.removeLinks(); });
        spaTemplate.removeLinks();
    }
}
