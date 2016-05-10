package com.bwg.iot;

import com.bwg.iot.model.Material;
import com.bwg.iot.model.SpaTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 5/3/16.
 */
public interface SpaTemplateRepository extends MongoRepository<SpaTemplate, String> {

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);
    public Page findByProductName(@Param("productName") String productName, Pageable p);
    public Page findByModel(@Param("model") String model, Pageable p);

    public Material findBySku(@Param("sku") String sku);

}
