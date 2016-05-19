package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by holow on 5/18/2016.
 */
@RepositoryRestResource(exported = false)
public interface AttachmentRepository extends MongoRepository<Attachment, String> {

}
