package com.bwg.iot;

import com.bwg.iot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by triton on 2/10/16.
 */
public interface UserRepository extends MongoRepository<User, String> {

}
