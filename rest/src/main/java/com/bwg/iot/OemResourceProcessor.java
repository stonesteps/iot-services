package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import com.bwg.iot.model.Oem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class OemResourceProcessor implements ResourceProcessor<Resource<Oem>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<Oem> process(Resource<Oem> resource) {
        final Oem oem = resource.getContent();
        if (oem.getLogo() != null) {
            resource.add(entityLinks.linkToSingleResource(Attachment.class, oem.getLogo().get_id()).withRel("logo"));
        }
        return resource;
    }

}
