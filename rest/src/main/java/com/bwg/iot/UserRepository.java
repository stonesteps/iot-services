package com.bwg.iot;

import com.bwg.iot.model.QUser;
import com.bwg.iot.model.User;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Iterator;

/**
 * Created by triton on 2/10/16.
 */
public interface UserRepository extends MongoRepository<User, String>, QueryDslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {
    @RestResource
    public User findByUsername(@Param("username") String username);

    @RestResource
    public Page findByDealerId(@Param("dealerId") String dealerId, Pageable p);

    @RestResource
    public Page findByOemId(@Param("oemId") String oemId, Pageable p);


    /*
     * (non-Javadoc)
     * @see org.springframework.data.web.querydsl.QuerydslBinderCustomizer#customize(org.springframework.data.web.querydsl.QuerydslBindings, com.mysema.query.types.EntityPath)
     */
    default void customize(QuerydslBindings bindings, QUser user) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(user.oemId).first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        bindings.bind(user.dealerId).first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(user.roles).first((path, value) -> {
            Iterator<String> it = value.iterator();
            BooleanExpression exp = path.contains(it.next());
            while (it.hasNext()) {
                exp = exp.and(path.contains(it.next()));
            }
            return exp;
        });
        bindings.bind(user.spaId).first((StringPath path, String value) -> {
            if (value.equalsIgnoreCase("null")) {
                return path.isNull();
            }
            return path.equalsIgnoreCase(value);
        });
    }
}
