package com.bwg.iot;

import com.bwg.iot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class FaultLogResourceProcessor implements ResourceProcessor<Resource<FaultLog>> {

    @Autowired
    private FaultLogDescriptionRepository faultLogDescriptionRepository;

    @Autowired
    private EntityLinks entityLinks;

    public Resource<FaultLog> process(Resource<FaultLog> resource) {
        final FaultLog faultLog = resource.getContent();
        final FaultLogDescription description = faultLogDescriptionRepository.findFirstByCodeAndControllerType(
                faultLog.getCode(), faultLog.getControllerType());
        faultLog.setFaultLogDescription(description);
        return resource;
    }

}
