package com.bwg.iot;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.Alert;
import com.bwg.iot.model.Owner;
import com.bwg.iot.model.Spa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

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
        Owner owner = spa.getOwner();
        if (owner != null) {
            owner.add(entityLinks.linkToSingleResource(Owner.class, owner.get_id()).withSelfRel());
            owner.add(entityLinks.linkToSingleResource(Address.class, owner.getAddress().get_id()).withRel("address"));
            resource.add(entityLinks.linkToSingleResource(Owner.class, owner.get_id()).withRel("owner"));
        }
        List<Alert> alerts = spa.getAlerts();
        for (int i=0; i<alerts.size(); i++) {
            Alert alert = alerts.get(i);
            alert.add(entityLinks.linkToSingleResource(Alert.class, alert.get_id()).withSelfRel());
            resource.add(entityLinks.linkToSingleResource(Alert.class, alert.get_id()).withRel("alert_"+(i+1)));
        }

        return resource;
    }

}
