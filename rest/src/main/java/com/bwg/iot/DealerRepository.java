package com.bwg.iot;

import com.bwg.iot.model.Dealer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by triton on 2/10/16.
 */
public interface DealerRepository extends MongoRepository<Dealer, String> {

}
