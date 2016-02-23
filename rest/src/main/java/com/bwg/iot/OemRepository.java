package com.bwg.iot;

import com.bwg.iot.model.Oem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by triton on 2/10/16.
 */
public interface OemRepository extends MongoRepository<Oem, String> {

}
