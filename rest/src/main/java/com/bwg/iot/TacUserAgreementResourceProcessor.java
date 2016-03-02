package com.bwg.iot;

import com.bwg.iot.model.TacUserAgreement;
import com.bwg.iot.model.TermsAndConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class TacUserAgreementResourceProcessor implements ResourceProcessor<Resource<TacUserAgreement>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<TacUserAgreement> process(Resource<TacUserAgreement> resource) {
        TacUserAgreement agreement = resource.getContent();
        resource.add(entityLinks.linkFor(TermsAndConditions.class).slash("/search/findMostRecent").withRel("terms"));
        return resource;
    }

}
