package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import com.bwg.iot.model.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class DealerResourceProcessor implements ResourceProcessor<Resource<Dealer>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<Dealer> process(Resource<Dealer> resource) {
        final Dealer dealer = resource.getContent();
        if (dealer.getLogo() != null) {
            resource.add(entityLinks.linkToSingleResource(Attachment.class, dealer.getLogo().get_id()).withRel("logo"));
        }
        return resource;
    }

}
