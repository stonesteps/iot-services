package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import com.bwg.iot.model.SpaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by triton on 3/1/16.
 */
@Component
public class SpaTemplateResourceProcessor implements ResourceProcessor<Resource<SpaTemplate>> {

    @Autowired
    private EntityLinks entityLinks;

    public Resource<SpaTemplate> process(Resource<SpaTemplate> resource) {
        SpaTemplate template = resource.getContent();
        if (template.getAttachments() != null) {
            template.getAttachments().forEach(attachment -> {
                attachment.add(entityLinks.linkToSingleResource(Attachment.class, attachment.get_id()).withSelfRel());
            });
        }
        return resource;
    }

}
