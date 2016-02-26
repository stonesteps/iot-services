package com.bwg.iot;

import com.bwg.iot.model.TacUserAgreement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by triton on 2/10/16.
 */
@RepositoryRestResource(exported = false)
public interface TacUserAgreementRepository extends MongoRepository<TacUserAgreement, String> {

}
