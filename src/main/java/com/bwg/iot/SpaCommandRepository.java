package com.bwg.iot;

import com.bwg.iot.model.Spa;
import com.bwg.iot.model.SpaCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by triton on 2/10/16.
 */
public interface SpaCommandRepository extends MongoRepository<SpaCommand, String> {


}
