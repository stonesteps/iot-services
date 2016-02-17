package com.bwg.iot;

import com.bwg.iot.model.LightOwner;
import com.bwg.iot.model.LightSpa;
import com.bwg.iot.model.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by triton on 2/10/16.
 */
//@RepositoryRestResource(excerptProjection = LightOwner.class)
public interface OwnerRepository extends MongoRepository<Owner, String> {

}
