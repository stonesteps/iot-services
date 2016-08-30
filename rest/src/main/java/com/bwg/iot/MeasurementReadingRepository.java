package com.bwg.iot;

import com.bwg.iot.model.MeasurementReading;
import com.bwg.iot.model.QMeasurementReading;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RepositoryRestResource(exported = false)
public interface MeasurementReadingRepository extends MongoRepository<MeasurementReading, String>,
        QueryDslPredicateExecutor<MeasurementReading>, QuerydslBinderCustomizer<QMeasurementReading> {

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

    @Override
    default public void customize(QuerydslBindings bindings, QMeasurementReading measurement) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(Date.class).all((DateTimePath<Date> path, Collection<? extends Date> value) -> {
            Iterator<? extends Date> it = value.iterator();
            Date start = it.next();
            if (it.hasNext()) {
                Date end = it.next();
                // make end date inclusive
                end.setMinutes(59);
                end.setHours(23);
                end.setSeconds(59);
                return path.between(start,end);
            } else {
                return path.after(start);
            }
        });
    }

}
