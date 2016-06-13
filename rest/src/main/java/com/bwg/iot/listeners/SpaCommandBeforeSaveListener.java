package com.bwg.iot.listeners;

import com.bwg.iot.model.SpaCommand;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Created by triton on 4/8/16.
 */
@Component
public class SpaCommandBeforeSaveListener extends AbstractMongoEventListener<SpaCommand> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<SpaCommand> event) {
        SpaCommand source = event.getSource();
        DBObject dbo = event.getDBObject();

        if ( source.getOriginatorId() == null ) {
            dbo.put(SpaCommand.REQUEST_ORIGINATOR, UUID.randomUUID().toString());
        }

        if ( source.getSentTimestamp() == null ) {
            dbo.put("sentTimestamp", new Date());
        }
    }
}
