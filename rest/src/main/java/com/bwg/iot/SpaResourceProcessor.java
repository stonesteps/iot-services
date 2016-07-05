package com.bwg.iot;

import com.bwg.iot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class SpaResourceProcessor implements ResourceProcessor<Resource<Spa>> {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private ComponentRepository componentRepository;

    public Resource<Spa> process(Resource<Spa> resource) {
        Spa spa = resource.getContent();
        User owner = spa.getOwner();
        if (owner != null) {
            owner.add(entityLinks.linkToSingleResource(User.class, owner.get_id()).withSelfRel());
            owner.add(entityLinks.linkToSingleResource(Address.class, owner.getAddress().get_id()).withRel("address"));
            resource.add(entityLinks.linkToSingleResource(User.class, owner.get_id()).withRel("owner"));
            resource.add(new Link(resource.getId().getHref() + "/faultLogs", "faultLogs"));
            resource.add(new Link(resource.getId().getHref() + "/wifiStats", "wifiStats"));
            resource.add(new Link(resource.getId().getHref() + "/events", "events"));
            resource.add(new Link(resource.getId().getHref() + "/measurements?measurementType=AC_CURRENT", "AC Current measurements"));
            resource.add(new Link(resource.getId().getHref() + "/measurements?measurementType=AMBIENT_TEMP", "Ambient Temp measurements"));
        }
        resource.add(new Link(resource.getId().getHref() + "/recipes", "recipes"));
        resource.add(new Link(resource.getId().getHref() + "/recipes/" + spa.get_id() + "/run", "turnOffSpa"));
        if (spa.getTemplateId() != null) {
            resource.add(entityLinks.linkToSingleResource(SpaTemplate.class, spa.getTemplateId()).withRel("spaTemplate"));
        }
        List<Alert> alerts = spa.getAlerts();
        List<Link> alertLinks = new ArrayList<>();
        alerts.stream().forEach(alert -> {
            alert.add(entityLinks.linkToSingleResource(Alert.class, alert.get_id()).withSelfRel());
        });

        if (spa.getCurrentState() != null) {
            List<ComponentState> cs = spa.getCurrentState().getComponents();

            List<String> ids = cs.stream()
                    .filter(comp -> !StringUtils.isEmpty(comp.getComponentId()))
                    .map(ComponentState::getComponentId).collect(toList());

            Map<String, com.bwg.iot.model.Component> lookup =
                    StreamSupport.stream(componentRepository.findAll(ids).spliterator(), false).collect(toMap(com.bwg.iot.model.Component::get_id, Function.identity()));

            cs.stream().forEach(state -> {
                if (state.getComponentId() != null) {
                    state.add(entityLinks.linkToSingleResource(com.bwg.iot.model.Component.class, state.getComponentId()).withRel("component"));

                    int ctr = 0;
                    com.bwg.iot.model.Component comp = lookup.get(state.getComponentId());
                    if ( comp != null) {
                        for (AssociatedSensor sensor : comp.getAssociatedSensors()) {
                            if (sensor.getSensorId() != null) {
                                PageRequest request = new PageRequest(0, 100, new Sort(new Order(Direction.DESC, "timestamp")));
                                state.add(linkTo(methodOn(MeasurementReadingController.class).getMeasurementsBySensorId(sensor.getSensorId(), request, null)).withRel("measurements_sensor_" + ctr++));
                            }
                        }
                    }
                }
            });
        }
        return resource;
    }

}
