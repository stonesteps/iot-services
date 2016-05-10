package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface FaultLogRepository extends MongoRepository<FaultLog, String> {

    List<FaultLog> findBySpaId(String spaId);

    Page<FaultLog> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

}
