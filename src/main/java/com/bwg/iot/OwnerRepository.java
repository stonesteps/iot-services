package com.bwg.iot;

import com.bwg.iot.model.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by triton on 2/10/16.
 */
public interface OwnerRepository extends MongoRepository<Owner, String> {

}
