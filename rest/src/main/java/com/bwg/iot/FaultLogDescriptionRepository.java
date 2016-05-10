package com.bwg.iot;

import com.bwg.iot.model.FaultLogDescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FaultLogDescriptionRepository extends MongoRepository<FaultLogDescription, String> {

    FaultLogDescription findFirstByCodeAndControllerType(int code, String controllerType);

}
