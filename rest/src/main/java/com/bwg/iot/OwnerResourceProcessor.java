package com.bwg.iot;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class OwnerResourceProcessor implements ResourceProcessor<Resource<Owner>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<Owner> process(Resource<Owner> resource) {
        Owner owner = resource.getContent();
        Address address = owner.getAddress();
        if (address != null) {
            owner.add(entityLinks.linkToSingleResource(Address.class, address.get_id()).withSelfRel());
            resource.add(entityLinks.linkToSingleResource(Address.class, address.get_id()).withRel("address"));
        }
        return resource;
    }

}
