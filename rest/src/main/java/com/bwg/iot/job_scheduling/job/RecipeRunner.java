package com.bwg.iot.job_scheduling.job;

import com.bwg.iot.SpaCommandRepository;
import com.bwg.iot.model.Recipe;
import com.bwg.iot.model.SpaCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Runs given recipe.
 */
public final class RecipeRunner implements ItemProcessor<Recipe, Recipe> {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeRunner.class);

    private final SpaCommandRepository spaCommandRepository;

    public RecipeRunner(final SpaCommandRepository spaCommandRepository) {
        this.spaCommandRepository = spaCommandRepository;
    }

    @Override
    public Recipe process(final Recipe recipe) throws Exception {
        LOG.info("Running a recipe {}", recipe);

        recipe.getSettings().forEach(spaCommand -> {
            spaCommand.getMetadata().put(SpaCommand.REQUESTED_BY, "Anonymous User");
            spaCommand.getMetadata().put(SpaCommand.REQUEST_PATH, "Direct");
        });
        spaCommandRepository.insert(recipe.getSettings());

        return recipe;
    }
}
