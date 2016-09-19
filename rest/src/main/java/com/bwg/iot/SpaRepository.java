package com.bwg.iot;

import com.bwg.iot.model.*;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by triton on 2/10/16.
 */
@RepositoryRestResource(excerptProjection = LiteSpa.class)
public interface SpaRepository extends MongoRepository<Spa, String> , QueryDslPredicateExecutor<Spa>,
        QuerydslBinderCustomizer<QSpa>{

    @RestResource
    public Page findByDealerId(@Param("dealerId") String dealerId, Pageable p);

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);

    @Query(value = "{ 'owner.email' : ?0 }")
    public Spa findByUsername(@Param("username") String username);

    public List<Spa> findByTemplateId(@Param("templateId") String templateId);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.web.querydsl.QuerydslBinderCustomizer#customize(org.springframework.data.web.querydsl.QuerydslBindings, com.mysema.query.types.EntityPath)
     */
    default void customize(QuerydslBindings bindings, QSpa spa) {
        bindings.bind(spa.sold).first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(Date.class).all((DateTimePath<Date> path, Collection<? extends Date> value) -> {
            Iterator<? extends Date> it = value.iterator();
            Date firstTimestamp = it.next();
            if (it.hasNext()) {
                Date secondTimestamp = it.next();
                // make end date inclusive
                secondTimestamp.setHours(23);
                secondTimestamp.setMinutes(59);
                secondTimestamp.setSeconds(59);
                return path.between(firstTimestamp,secondTimestamp);
            } else {
                return path.after(firstTimestamp);
            }
        });
    }
}
