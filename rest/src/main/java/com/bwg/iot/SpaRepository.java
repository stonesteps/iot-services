package com.bwg.iot;

import com.bwg.iot.model.*;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.StringPath;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by triton on 2/10/16.
 */
public interface SpaRepository extends MongoRepository<Spa, String> , QueryDslPredicateExecutor<Spa>,
        QuerydslBinderCustomizer<QSpa>{

    @RestResource
    public Page findByDealerId(@Param("dealerId") String dealerId, Pageable p);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.web.querydsl.QuerydslBinderCustomizer#customize(org.springframework.data.web.querydsl.QuerydslBindings, com.mysema.query.types.EntityPath)
     */
    default void customize(QuerydslBindings bindings, QSpa spa) {
        bindings.bind(spa.sold).first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
