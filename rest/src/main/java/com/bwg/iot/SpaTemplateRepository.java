package com.bwg.iot;

import com.bwg.iot.model.SpaTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 5/3/16.
 */
public interface SpaTemplateRepository extends MongoRepository<SpaTemplate, String> {

    @Query(value = "{ 'oemId' : ?0 , 'active': true }")
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);

    public Page findByProductName(@Param("productName") String productName, Pageable p);

    public Page findByModel(@Param("model") String model, Pageable p);

    public SpaTemplate findBySku(@Param("sku") String sku);

    @Override
    @RestResource(exported = false)
    void delete(String id);

    @Override
    @RestResource(exported = false)
    void delete(SpaTemplate spaTemplate);


}
