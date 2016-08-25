package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.QFaultLog;
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
import java.util.List;

@RepositoryRestResource(exported = false)
public interface FaultLogRepository extends MongoRepository<FaultLog, String>,
        QueryDslPredicateExecutor<FaultLog>, QuerydslBinderCustomizer<QFaultLog> {

    List<FaultLog> findBySpaId(String spaId);

    Page<FaultLog> findBySpaId(@Param("spaId") String spaId, Pageable pageable);

    @Override
    default public void customize(QuerydslBindings bindings, QFaultLog fault) {
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
