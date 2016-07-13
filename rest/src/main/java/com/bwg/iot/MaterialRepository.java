package com.bwg.iot;

import com.bwg.iot.model.LiteMaterial;
import com.bwg.iot.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 5/3/16.
 */
@RepositoryRestResource(excerptProjection = LiteMaterial.class)
public interface MaterialRepository extends MongoRepository<Material, String> {

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);
    public Page findByOemIdAndComponentType(@Param("oemId") String oemId, @Param("componentType") String type, Pageable p);
    public Page findByOemIdAndMaterialType(@Param("oemId") String oemId, @Param("materialType") String type, Pageable p);

    public Material findBySku(@Param("sku") String sku);

}
