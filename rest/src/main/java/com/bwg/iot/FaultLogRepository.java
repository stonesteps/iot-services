package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface FaultLogRepository extends MongoRepository<FaultLog, String> {

    @RestResource
    Page<FaultLog> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

    @Override
    @RestResource(exported = false)
    FaultLog insert(FaultLog entity);

    @Override
    @RestResource(exported = false)
    FaultLog save(FaultLog entity);

    @Override
    @RestResource(exported = false)
    Page<FaultLog> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    List<FaultLog> findAll();

    @Override
    @RestResource(exported = false)
    List<FaultLog> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    void delete(String s);

    @Override
    @RestResource(exported = false)
    void delete(FaultLog entity);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends FaultLog> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
