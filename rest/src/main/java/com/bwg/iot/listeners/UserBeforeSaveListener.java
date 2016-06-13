package com.bwg.iot.listeners;

import com.bwg.iot.AddressRepository;
import com.bwg.iot.model.Address;
import com.bwg.iot.model.SpaCommand;
import com.bwg.iot.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by triton on 4/8/16.
 */
@Component
public class UserBeforeSaveListener extends AbstractMongoEventListener<User> {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public void onBeforeSave(BeforeSaveEvent<User> event) {
        User source = event.getSource();
        DBObject dbo = event.getDBObject();

        // persist Address in its own collection
        Address address = source.getAddress();
        if (address != null && StringUtils.isEmpty(address.get_id())) {
            address = addressRepository.save(address);

            BasicDBObject dboAddr = (BasicDBObject) dbo.get("address");
            dboAddr.put("_id", address.get_id());
        }

        // set createdDate
        dbo.put("createdDate", new Date());
    }
}
