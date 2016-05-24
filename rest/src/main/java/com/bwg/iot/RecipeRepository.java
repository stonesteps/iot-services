package com.bwg.iot;

import com.bwg.iot.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by triton on 2/10/16.
 */
@RepositoryRestResource(exported = false)
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    public Page findBySpaId(@Param("spaId") String spaId, Pageable p);
}
