package com.bwg.iot;

import com.bwg.iot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class SpaResourceProcessor implements ResourceProcessor<Resource<Spa>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<Spa> process(Resource<Spa> resource) {
        Spa spa = resource.getContent();
        User owner = spa.getOwner();
        if (owner != null) {
            owner.add(entityLinks.linkToSingleResource(User.class, owner.get_id()).withSelfRel());
            owner.add(entityLinks.linkToSingleResource(Address.class, owner.getAddress().get_id()).withRel("address"));
            resource.add(entityLinks.linkToSingleResource(User.class, owner.get_id()).withRel("owner"));
        }

        List<Alert> alerts = spa.getAlerts();
        List<Link> alertLinks = new ArrayList<>();
        alerts.stream().forEach(alert -> {
            alert.add(entityLinks.linkToSingleResource(Alert.class, alert.get_id()).withSelfRel());
        });

        if (spa.getCurrentState() != null) {
            List<ComponentState> cs = spa.getCurrentState().getComponents();
            cs.stream().forEach(state -> {
                if (state.getSerialNumber() != null) {
                    state.add(entityLinks.linkFor(com.bwg.iot.model.Component.class)
                            .slash("/search/findBySerialNumber?serialNumber=" + state.getSerialNumber())
                            .withRel(state.getSerialNumber()));
                }
            });
        }
        return resource;
    }

}
