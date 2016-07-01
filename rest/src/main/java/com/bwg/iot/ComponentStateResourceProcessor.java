package com.bwg.iot;

import com.bwg.iot.model.ComponentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * decorate associated sensors with a hateoas link to their associated measurements
 */
@Component
public class ComponentStateResourceProcessor implements ResourceProcessor<Resource<ComponentState>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<ComponentState> process(Resource<ComponentState> resource) {
        final ComponentState state = resource.getContent();

        if (state.getComponentId() != null) {
            resource.add(entityLinks.linkToSingleResource(com.bwg.iot.model.Component.class, state.getComponentId()).withRel("component"));
        }
        return resource;
    }
}
