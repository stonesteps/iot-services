package com.bwg.iot.job_scheduling.job;

import com.bwg.iot.SpaCommandRepository;
import com.bwg.iot.job_scheduling.JobSchedulingComponent;
import com.bwg.iot.model.Recipe;
import com.bwg.iot.model.SpaCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Runs given recipe and schedules spa turn off.
 */
public final class RecipeRunner implements ItemProcessor<Recipe, Recipe> {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeRunner.class);

    private final JobSchedulingComponent jobSchedulingComponent;
    private final SpaCommandRepository spaCommandRepository;

    public RecipeRunner(final JobSchedulingComponent jobSchedulingComponent, final SpaCommandRepository spaCommandRepository) {
        this.jobSchedulingComponent = jobSchedulingComponent;
        this.spaCommandRepository = spaCommandRepository;
    }

    @Override
    public Recipe process(final Recipe recipe) throws Exception {
        LOG.info("Processing a recipe {}, id:{}", recipe.getName(), recipe.get_id());

        recipe.getSettings().forEach(spaCommand -> {
            spaCommand.set_id(null); // null the id just in case
            spaCommand.getMetadata().put(SpaCommand.REQUESTED_BY, "Scheduled Job");
            spaCommand.getMetadata().put(SpaCommand.REQUEST_PATH, "Direct");
            LOG.info("Send Spa Command: {}", spaCommand.getRequestTypeId());
        });
        spaCommandRepository.insert(recipe.getSettings());

        // schedule spa turn off
        jobSchedulingComponent.scheduleSpaTurnOff(recipe.getSpaId());

        return recipe;
    }
}
