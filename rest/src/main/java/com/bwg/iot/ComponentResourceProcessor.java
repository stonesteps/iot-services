package com.bwg.iot;

import com.bwg.iot.model.AssociatedSensor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * decorate associated sensors with a hateoas link to their associated measurements
 */
@Component
public class ComponentResourceProcessor implements ResourceProcessor<Resource<com.bwg.iot.model.Component>> {

    public Resource<com.bwg.iot.model.Component> process(Resource<com.bwg.iot.model.Component> resource) {
        final com.bwg.iot.model.Component comp = resource.getContent();

        for (AssociatedSensor sensor : comp.getAssociatedSensors()) {
            if (sensor.getSensorId() != null) {
                PageRequest request = new PageRequest(0, 100, new Sort(new Order(Direction.DESC, "timestamp")));
                sensor.add(linkTo(methodOn(MeasurementReadingController.class).getMeasurementsBySensorId(sensor.getSensorId(), request, null)).withRel("measurements"));
            }
        }
        return resource;
    }
}
