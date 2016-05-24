package com.bwg.iot;

import com.bwg.iot.model.WifiStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface WifiStatRepository extends MongoRepository<WifiStat, String> {

    Page<WifiStat> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

}
