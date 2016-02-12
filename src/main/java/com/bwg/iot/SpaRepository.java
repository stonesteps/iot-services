package com.bwg.iot;

import com.bwg.iot.model.Spa;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by triton on 2/10/16.
 */
public interface SpaRepository extends MongoRepository<Spa, String> {

    public List<Spa> findByDealerId(@Param("dealerId") String dealerId);

}
