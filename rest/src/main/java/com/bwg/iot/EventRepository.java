package com.bwg.iot;

import com.bwg.iot.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface EventRepository extends MongoRepository<Event, String> {

    Page<Event> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

}
