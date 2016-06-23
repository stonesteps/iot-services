package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import com.bwg.iot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class UserResourceProcessor implements ResourceProcessor<Resource<User>> {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    CommonHelper commonHelper;

    public Resource<User> process(Resource<User> resource) {
        final User user = resource.getContent();

        Link spaLink = commonHelper.getMySpaLink(user);
        if (spaLink != null) {
            resource.add(spaLink);
        }

        Link logoLink = commonHelper.getMyLogoLink(user);
        if (logoLink != null) {
            resource.add(logoLink);
        }
        return resource;
    }

}
