package com.bwg.iot;

import com.bwg.iot.model.Oem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 2/10/16.
 */
public interface OemRepository extends MongoRepository<Oem, String> {
    public Oem findByCustomerNumber(@Param("customerNumber") Integer customerNumber);

    @Override
    @RestResource(exported = false)
    void delete(String id);

    @Override
    @RestResource(exported = false)
    void delete(Oem oem);


}
