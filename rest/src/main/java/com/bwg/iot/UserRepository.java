package com.bwg.iot;

import com.bwg.iot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 2/10/16.
 */
public interface UserRepository extends MongoRepository<User, String> {
    @RestResource
    public User findByUsername(@Param("username") String username);

    @RestResource
    public Page findByDealerId(@Param("dealerId") String dealerId, Pageable p);

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);

}
