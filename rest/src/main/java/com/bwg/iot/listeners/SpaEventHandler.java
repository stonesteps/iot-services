package com.bwg.iot.listeners;

import com.bwg.iot.ComponentRepository;
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

/**
 * Created by triton on 4/8/16.
 */
@RepositoryEventHandler(Spa.class)
public class SpaEventHandler {
    private static final Logger log = LoggerFactory.getLogger(SpaEventHandler.class);

    @Autowired
    ComponentRepository componentRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(Spa spa) {
        log.debug("Before Create: " + spa.toString());

        // persist Components in its own collection
//        List<Component> components = spa.getC;
//        if (address != null && StringUtils.isEmpty(address.get_id())) {
//            address = addressRepository.save(address);
//            user.setAddress(address);
//        }
//
//        // set createdDate
//        user.setCreatedDate(new Date());
    }

    @HandleAfterCreate
    public void handleAfterCreate(User user) {
        log.debug("After Create: " + user.getUsername());
    }
}
