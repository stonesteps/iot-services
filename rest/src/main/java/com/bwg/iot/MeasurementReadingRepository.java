package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface MeasurementReadingRepository extends MongoRepository<MeasurementReading, String> {

    Page<MeasurementReading> findBySpaIdAndMoteIdAndType(@Param("spaId") String spaId, @Param("moteId") String moteId, @Param("type") String type, Pageable pageable);

    Page<MeasurementReading> findBySpaIdAndType(@Param("spaId") String spaId, @Param("type") String type, Pageable pageable);

}
