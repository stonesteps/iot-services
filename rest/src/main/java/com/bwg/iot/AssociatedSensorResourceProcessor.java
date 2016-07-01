package com.bwg.iot;

import com.bwg.iot.model.AssociatedSensor;
import com.bwg.iot.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * decorate associated sensors with the links to their associated measurements
 */
@Component
public class AssociatedSensorResourceProcessor implements ResourceProcessor<Resource<com.bwg.iot.model.Component>> {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    PersistentEntityResourceAssembler entityAssembler;

    public Resource<com.bwg.iot.model.Component> process(Resource<com.bwg.iot.model.Component> resource) {
        final com.bwg.iot.model.Component comp = resource.getContent();
        for (AssociatedSensor sensor : comp.getAssociatedSensors()) {
            if (sensor.getMoteId() != null && sensor.getSensorId() != null) {
                PageRequest request = new PageRequest(0, 100, new Sort(new Order(Direction.DESC, "timestamp")));
                linkTo(methodOn(MeasurementReadingController.class).getMeasurements(Optional.empty(), Optional.empty(), Optional.of(sensor.getMoteId()), Optional.of(sensor.getSensorId()), request, entityAssembler)).withRel("measurements");
            }
        }
        return resource;
    }
}
