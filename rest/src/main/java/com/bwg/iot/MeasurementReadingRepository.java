package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface MeasurementReadingRepository extends MongoRepository<MeasurementReading, String> {

    /**
     * retrieve sensors readings by moteId and the type of sensor data
     *
     * @param moteId
     * @param type
     * @param pageable
     * @return
     */
    Page<MeasurementReading> findByMoteIdAndType(@Param("moteId") String moteId, @Param("type") String type, Pageable pageable);

    /**
     * retrieve sensors readings by moteId
     *
     * @param moteId
     * @param pageable
     * @return
     */
    Page<MeasurementReading> findByMoteId(@Param("moteId") String moteId, Pageable pageable);

    /**
     * retrieve sensor readings by the spaId and type of reading, this will include multiple motes/sensors in reesult set
     *
     * @param spaId
     * @param type
     * @param pageable
     * @return
     */
    Page<MeasurementReading> findBySpaIdAndType(@Param("spaId") String spaId, @Param("type") String type, Pageable pageable);

    /**
     * retrieve sensor readings by the spaId, this will include multiple motes/sensors in reesult set
     *
     * @param spaId
     * @param pageable
     * @return
     */
    Page<MeasurementReading> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

    /**
     * retrieve sensor readings by the sensorId
     *
     * @param sensorId
     * @param pageable
     * @return
     */
    Page<MeasurementReading> findBySensorId(@Param("sensorId") String sensorId, Pageable pageable);


}
