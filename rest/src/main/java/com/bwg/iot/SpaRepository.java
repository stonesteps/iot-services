package com.bwg.iot;

import com.bwg.iot.model.*;
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

/**
 * Created by triton on 2/10/16.
 */
@RepositoryRestResource(excerptProjection = LiteSpa.class)
public interface SpaRepository extends MongoRepository<Spa, String> , QueryDslPredicateExecutor<Spa>,
        QuerydslBinderCustomizer<QSpa>{

    @RestResource
    public Page findByDealerId(@Param("dealerId") String dealerId, Pageable p);

    @Query(value = "{ 'owner.username' : ?0 }")
    public Spa findByUsername(@Param("username") String username);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.web.querydsl.QuerydslBinderCustomizer#customize(org.springframework.data.web.querydsl.QuerydslBindings, com.mysema.query.types.EntityPath)
     */
    default void customize(QuerydslBindings bindings, QSpa spa) {
        bindings.bind(spa.sold).first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
