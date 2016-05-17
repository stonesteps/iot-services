package com.bwg.iot;

import com.bwg.iot.model.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by triton on 2/10/16.
 */
public interface ComponentRepository extends MongoRepository<Component, String> {

    @RestResource
    public Page findBySpaIdOrderByComponentType(@Param("spaId") String spaId, Pageable p);
    public Page findBySpaIdAndComponentTypeOrderByPortAsc(@Param("spaId") String spaId, @Param("componentType") String type, Pageable p);
    public List<Component> findBySpaId(@Param("spaId") String spaId);
    public Component findBySerialNumber(@Param("serialNumber") String serialNumber);

}
