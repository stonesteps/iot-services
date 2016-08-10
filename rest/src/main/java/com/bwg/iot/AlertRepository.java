package com.bwg.iot;

import com.bwg.iot.model.Alert;
import com.bwg.iot.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 2/10/16.
 */
public interface AlertRepository extends MongoRepository<Alert, String> {

    Page<Alert> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

    Page<Alert> findByDealerId(@Param("dealerId") String dealerId, Pageable pageable);

    Page<Alert> findByOemId(@Param("oemId") String oemId, Pageable pageable);
}
