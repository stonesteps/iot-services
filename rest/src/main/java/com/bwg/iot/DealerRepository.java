package com.bwg.iot;

import com.bwg.iot.model.Dealer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 2/10/16.
 */
public interface DealerRepository extends MongoRepository<Dealer, String> {

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);

    @Override
    @RestResource(exported = false)
    void delete(String id);

    @Override
    @RestResource(exported = false)
    void delete(Dealer dealer);

}
